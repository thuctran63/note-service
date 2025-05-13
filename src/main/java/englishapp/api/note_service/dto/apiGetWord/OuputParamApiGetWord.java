package englishapp.api.note_service.dto.apiGetWord;

import englishapp.api.note_service.models.Description;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OuputParamApiGetWord {
    String word;
    Description description;
    String pronounce;
}
