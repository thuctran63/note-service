package englishapp.api.note_service.models;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LearningWord {
    @Id
    private String wordId;
    private String word;
    private String html;
    private String description;
    private boolean learned;
    private String pronounce;
    private String note;
    private String example;
}
