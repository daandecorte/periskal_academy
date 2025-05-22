package ap.student.project.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExamAnswerDTO {
    @JsonProperty("questionId")
    private int questionId;

    @JsonProperty("optionId")
    private int optionId;

    public ExamAnswerDTO() {
    }

    public ExamAnswerDTO(int questionId, int optionId) {
        this.questionId = questionId;
        this.optionId = optionId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }
}