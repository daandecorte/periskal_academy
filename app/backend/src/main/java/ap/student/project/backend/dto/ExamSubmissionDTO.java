package ap.student.project.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ExamSubmissionDTO {
    @JsonProperty("examId")
    private int examId;

    @JsonProperty("userId")
    private int userId;

    @JsonProperty("answers")
    private List<ExamAnswerDTO> answers;

    public ExamSubmissionDTO() {
    }

    public ExamSubmissionDTO(int examId, int userId, List<ExamAnswerDTO> answers) {
        this.examId = examId;
        this.userId = userId;
        this.answers = answers;
    }

    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<ExamAnswerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<ExamAnswerDTO> answers) {
        this.answers = answers;
    }
}
