package englishapp.api.note_service.services;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import englishapp.api.note_service.dto.apiGetWord.OuputParamApiGetWord;
import englishapp.api.note_service.dto.apiSearchWord.OutputParamApiSearchWord;
import englishapp.api.note_service.dto.apiSearchWord.WordSearch;
import englishapp.api.note_service.models.Description;
import englishapp.api.note_service.repositories.WordRepository;
import englishapp.api.note_service.util.GeminiApiService;
import reactor.core.publisher.Mono;

@Service
public class WordService {

    @Qualifier("reactiveRedisTemplate")
    @Autowired
    private ReactiveRedisTemplate<String, String> redisTemplate;

    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private GeminiApiService geminiApiService;

    @Autowired
    private ObjectMapper objectMapper;

    public Mono<OutputParamApiSearchWord> searchWord(String word) {
        return redisTemplate.opsForValue().get(word)
                .flatMap(cachedWordsJson -> {
                    List<WordSearch> cachedWords = parseWordsFromJson(cachedWordsJson);
                    OutputParamApiSearchWord result = new OutputParamApiSearchWord();
                    result.setWords(cachedWords);
                    return Mono.just(result);
                })
                .switchIfEmpty(
                        wordRepository.findTop10ByWordStartingWithIgnoreCaseOrderByWordAsc(word)
                                .collectList()
                                .flatMap(words -> {
                                    if (words.isEmpty()) {
                                        return Mono.empty();
                                    } else {
                                        List<WordSearch> wordList = words.stream()
                                                .map(w -> new WordSearch(w.getWordId(), w.getWord()))
                                                .collect(Collectors.toList());

                                        String wordsJson = convertWordsToJson(wordList);
                                        OutputParamApiSearchWord result = new OutputParamApiSearchWord();
                                        result.setWords(wordList);

                                        return redisTemplate.opsForValue()
                                                .set(word, wordsJson, Duration.ofHours(1))
                                                .thenReturn(result);
                                    }
                                }));
    }

    public Mono<OuputParamApiGetWord> getWord(String wordId) {
        return wordRepository.findByWordId(wordId)
                .flatMap(word -> {
                    OuputParamApiGetWord result = new OuputParamApiGetWord();
                    result.setWord(word.getWord());

                    // If description is empty, fetch it from Gemini API
                    if (Objects.isNull(word.getDescription())) {
                        return geminiApiService.getDescriptionFromGemini(word.getWord())
                                .doOnNext(description -> {
                                    Description descriptionObject = parseDescription(description);
                                    word.setDescription(descriptionObject);
                                    wordRepository.save(word).subscribe();
                                })
                                .map(description -> {
                                    Description descriptionObject = parseDescription(description);
                                    result.setDescription(descriptionObject);
                                    result.setPronounce(word.getPronounce());
                                    return result;
                                });
                    } else {
                        result.setDescription(word.getDescription());
                        result.setPronounce(word.getPronounce());
                        return Mono.just(result);
                    }
                });
    }

    private String convertWordsToJson(List<WordSearch> words) {
        try {
            return objectMapper.writeValueAsString(words);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting words to JSON", e);
        }
    }

    private List<WordSearch> parseWordsFromJson(String wordsJson) {
        try {
            return objectMapper.readValue(wordsJson, new TypeReference<List<WordSearch>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing JSON to words", e);
        }
    }

    public Description parseDescription(String input) {
        Description description = new Description();

        // Regex patterns for each section
        String meanPattern = "Mean:\\s*(.*?)\\n";
        String examplePattern = "Example:\\s*(.*?)\\n";
        String wordTypePattern = "WordType:\\s*(.*?)\\n";
        String synonymsPattern = "Synonyms:\\s*(.*?)\\n";
        String antonymsPattern = "Antonym:\\s*(.*?)\\n";

        // Parse Mean
        Pattern pattern = Pattern.compile(meanPattern);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            String mean = matcher.group(1).trim();
            description.setMean(mean);
        }

        // Parse Example
        pattern = Pattern.compile(examplePattern);
        matcher = pattern.matcher(input);
        if (matcher.find()) {
            String example = matcher.group(1).trim();
            description.setExample(example);
        }

        // Parse WordType
        pattern = Pattern.compile(wordTypePattern);
        matcher = pattern.matcher(input);
        if (matcher.find()) {
            String wordType = matcher.group(1).trim();
            description.setWordType(wordType);
        }

        // Parse Synonyms
        pattern = Pattern.compile(synonymsPattern);
        matcher = pattern.matcher(input);
        if (matcher.find()) {
            String synonyms = matcher.group(1);
            description.setSynonyms(synonyms);
        }

        // Parse Antonyms
        pattern = Pattern.compile(antonymsPattern);
        matcher = pattern.matcher(input);
        if (matcher.find()) {
            String antonyms = matcher.group(1);
            description.setAntonym(antonyms);
        }

        return description;
    }
}
