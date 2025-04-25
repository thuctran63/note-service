package englishapp.api.note_service.repositories;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import englishapp.api.note_service.models.Note;
import reactor.core.publisher.Mono;

@Repository
public interface NoteRepository extends ReactiveMongoRepository<Note, String> {
    @Query("{ 'userId': ?0 }")
    Mono<Note> findByUserId(String userId);
}
