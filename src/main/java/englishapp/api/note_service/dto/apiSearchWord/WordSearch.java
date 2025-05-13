package englishapp.api.note_service.dto.apiSearchWord;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WordSearch {
    private String wordId;
    private String word;
}
