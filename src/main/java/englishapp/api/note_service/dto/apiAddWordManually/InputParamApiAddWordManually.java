package englishapp.api.note_service.dto.apiAddWordManually;

import lombok.Data;

@Data
public class InputParamApiAddWordManually {
    private String word;
    private String description;
    private String pronounce;
    private String note;
    private String example;
    private boolean learned;

    // getters and setters learnded
    public boolean getLearned() {
        return learned;
    }

    public void setLearned(boolean learned) {
        this.learned = learned;
    }

}
