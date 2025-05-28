package ap.student.project.backend.dto;

public class ExamResultDTO {
    private int score;
    private boolean passed;
    private Integer certificateId;
    private Integer attemptId;
    
    public ExamResultDTO() {
    }
    
    public ExamResultDTO(int score, boolean passed) {
        this.score = score;
        this.passed = passed;
    }
    
    public int getScore() {
        return score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public boolean isPassed() {
        return passed;
    }
    
    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public Integer getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(Integer certificateId) {
        this.certificateId = certificateId;
    }

    public Integer getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(Integer attemptId) {
        this.attemptId = attemptId;
    }
}