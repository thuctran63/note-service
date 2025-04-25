package englishapp.api.note_service.dto.apiUpdateWord;

import lombok.Data;

@Data
public class InputParamApiUpdateWord {
    private String wordId;
    private String word;
    private String description;
    private Boolean learned;
    private String pronounce;
    private String note;
    private String example;
}
