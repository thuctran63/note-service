package englishapp.api.note_service.models;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "notes")
public class Note {
    @Id
    String noteId;
    String userId;
    List<LearningWord> words;
    Double percentLearned;
}
