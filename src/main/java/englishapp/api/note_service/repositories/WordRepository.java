package englishapp.api.note_service.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import englishapp.api.note_service.models.Word;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface WordRepository extends ReactiveMongoRepository<Word, String> {
    Flux<Word> findTop10ByWordStartingWithIgnoreCaseOrderByWordAsc(String prefix);

    Mono<Word> findByWordId(String wordId);
}
