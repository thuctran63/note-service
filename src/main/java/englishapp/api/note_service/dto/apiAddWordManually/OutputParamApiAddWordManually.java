package englishapp.api.note_service.dto.apiAddWordManually;

import englishapp.api.note_service.models.LearningWord;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OutputParamApiAddWordManually {
    LearningWord word;
}
