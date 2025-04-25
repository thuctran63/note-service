package englishapp.api.note_service.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import englishapp.api.note_service.models.Word;

public interface WordRepository extends ReactiveMongoRepository<Word, String> {
    // Define any custom query methods if needed
}
