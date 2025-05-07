package ap.student.project.backend.dto;

public class ExamAnswerDTO {
    private int questionId;
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