package englishapp.api.note_service.dto.apiGetNote;

import englishapp.api.note_service.models.Note;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OutputParamApiGetNote {
    private Note note;
}
