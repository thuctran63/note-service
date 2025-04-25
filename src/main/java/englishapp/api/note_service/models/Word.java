package englishapp.api.note_service.models;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Word {
    @Id
    String wordId;
    String word;
    String html;
    String description;
    String pronounce;
}
