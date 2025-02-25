package ap.student.project.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "exam_attempt")
public class ExamAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "start_date_time")
    private LocalDateTime startDateTime;
    @Column(name = "end_date_time")
    private LocalDateTime endDateTime;
    @Enumerated(EnumType.STRING)
    @Column(name = "exam_status_type")
    private ExamStatusType examStatusType;
    @Column
    private int score;

    public ExamAttempt() {
    }

    public ExamAttempt(LocalDateTime startDateTime, LocalDateTime endDateTime, ExamStatusType examStatusType, int score) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.examStatusType = examStatusType;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public ExamStatusType getExamStatusType() {
        return examStatusType;
    }

    public void setExamStatusType(ExamStatusType examStatusType) {
        this.examStatusType = examStatusType;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "ExamAttempt{" +
                "id=" + id +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                ", examStatusType=" + examStatusType +
                ", score=" + score +
                '}';
    }
}
