package ap.student.project.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "user_exam")
@Getter
@Setter
@NoArgsConstructor
public class UserExam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exam_id")
    private Exam exam;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "exam_attempt")
    private List<ExamAttempt> examAttempts;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinTable(name = "user_id")
    private User user;

    public UserExam(Exam exam, List<ExamAttempt> examAttempts, User user) {
        this.exam = exam;
        this.examAttempts = examAttempts;
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserExam{" +
                "id=" + id +
                ", exam=" + exam +
                ", examAttempts=" + examAttempts +
                ", user=" + user +
                '}';
    }
}
