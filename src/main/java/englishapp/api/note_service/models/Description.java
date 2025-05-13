package englishapp.api.note_service.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Description {
    private String mean;
    private String example;
    private String wordType;
    private String synonyms;
    private String antonym;
}