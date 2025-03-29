package ap.student.project.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "exam_attempt")
@Getter
@Setter
@NoArgsConstructor
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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_module_id")
    private UserModule userModule;

    public ExamAttempt(LocalDateTime startDateTime, LocalDateTime endDateTime, ExamStatusType examStatusType, int score, UserModule userModule) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.examStatusType = examStatusType;
        this.score = score;
        this.userModule = userModule;
    }

    @Override
    public String toString() {
        return "ExamAttempt{" +
                "id=" + id +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                ", examStatusType=" + examStatusType +
                ", score=" + score +
                ", userExam=" + userModule +
                '}';
    }
}
