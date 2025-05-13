package englishapp.api.note_service.dto.apiSearchWord;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutputParamApiSearchWord {
    private List<WordSearch> words;
}
