package englishapp.api.note_service.services;

import java.util.ArrayList;
import java.util.Objects;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import englishapp.api.note_service.dto.apiAddWordManually.InputParamApiAddWordManually;
import englishapp.api.note_service.dto.apiDeleteWord.InputParamApiDeleteWord;
import englishapp.api.note_service.dto.apiGetNote.OutputParamApiGetNote;
import englishapp.api.note_service.dto.apiUpdateWord.InputParamApiUpdateWord;
import englishapp.api.note_service.models.LearningWord;
import englishapp.api.note_service.models.Note;
import englishapp.api.note_service.repositories.NoteRepository;
import reactor.core.publisher.Mono;

@Service
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;

    public Mono<OutputParamApiGetNote> getNote(String userId) {
        return noteRepository.findByUserId(userId)
                .map(OutputParamApiGetNote::new)
                .switchIfEmpty(
                        Mono.defer(() -> {
                            Note note = new Note();
                            note.setUserId(userId);
                            note.setPercentLearned(0.0);
                            note.setWords(new ArrayList<>());
                            return noteRepository.save(note)
                                    .map(OutputParamApiGetNote::new);
                        }));
    }

    public Mono<LearningWord> addWordManually(String userId, InputParamApiAddWordManually inputParam) {
        return noteRepository.findByUserId(userId)
                .flatMap(note -> {
                    LearningWord word = new LearningWord();
                    word.setWordId(ObjectId.get().toString());
                    word.setWord(inputParam.getWord());
                    word.setHtml("");
                    word.setDescription(inputParam.getDescription());
                    word.setLearned(inputParam.getLearned());
                    word.setPronounce(inputParam.getPronounce());
                    word.setNote(inputParam.getNote());
                    word.setExample(inputParam.getExample());

                    note.getWords().add(word);
                    return noteRepository.save(note)
                            .thenReturn(word); // Trả về LearningWord sau khi save
                })
                .switchIfEmpty(Mono.empty()); // Trả về Mono.empty() nếu không tìm thấy note
    }

    public Mono<LearningWord> deleteWord(String userId, InputParamApiDeleteWord inputParam) {
        return noteRepository.findByUserId(userId)
                .flatMap(note -> {
                    LearningWord wordToDelete = null;
                    for (LearningWord word : note.getWords()) {
                        if (word.getWordId().equals(inputParam.getWordId())) {
                            wordToDelete = word;
                            break;
                        }
                    }

                    if (wordToDelete != null) {
                        note.getWords().remove(wordToDelete);
                        return noteRepository.save(note)
                                .thenReturn(wordToDelete); // Trả về LearningWord đã xóa
                    } else {
                        return Mono.empty(); // Trả về Mono.empty() nếu không tìm thấy từ để xóa
                    }
                })
                .switchIfEmpty(Mono.empty()); // Trả về Mono.empty() nếu không tìm thấy note
    }

    public Mono<LearningWord> updateWord(String userId, InputParamApiUpdateWord inputParam) {
        return noteRepository.findByUserId(userId)
                .flatMap(
                        note -> {
                            LearningWord wordToUpdate = null;
                            for (LearningWord word : note.getWords()) {
                                if (word.getWordId().equals(inputParam.getWordId())) {
                                    wordToUpdate = word;
                                    break;
                                }
                            }

                            if (wordToUpdate != null) {
                                if (Objects.nonNull(inputParam.getWord())) {
                                    wordToUpdate.setWord(inputParam.getWord());
                                }
                                if (Objects.nonNull(inputParam.getDescription())) {
                                    wordToUpdate.setDescription(inputParam.getDescription());
                                }

                                if (Objects.nonNull(inputParam.getLearned())) {
                                    wordToUpdate.setLearned(inputParam.getLearned());
                                }

                                if (Objects.nonNull(inputParam.getPronounce())) {
                                    wordToUpdate.setPronounce(inputParam.getPronounce());
                                }

                                if (Objects.nonNull(inputParam.getNote())) {
                                    wordToUpdate.setNote(inputParam.getNote());
                                }

                                return noteRepository.save(note)
                                        .thenReturn(wordToUpdate); // Trả về LearningWord đã cập nhật
                            } else {
                                return Mono.empty(); // Trả về Mono.empty() nếu không tìm thấy từ để cập nhật
                            }
                        })
                .switchIfEmpty(Mono.empty()); // Trả về Mono.empty() nếu không tìm thấy note
    }

}
