package ap.student.project.backend.dto;

public class ExamResultDTO {
    private int score;
    private boolean passed;
    
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
}