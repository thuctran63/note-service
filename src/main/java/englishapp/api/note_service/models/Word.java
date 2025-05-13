package englishapp.api.note_service.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Document(collection = "words")
public class Word {
    @Id
    String wordId;
    @Indexed
    String word;
    Description description;
    String pronounce;
}
