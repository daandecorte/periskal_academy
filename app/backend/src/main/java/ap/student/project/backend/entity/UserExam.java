package ap.student.project.backend.entity;
/*
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @OneToMany(mappedBy = "userExam", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<ExamAttempt> examAttempts;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_module")
    @JsonIgnore
    private UserModule userModule;

    public UserExam(Exam exam, List<ExamAttempt> examAttempts, UserModule userModule) {
        this.exam = exam;
        this.examAttempts = examAttempts;
        this.userModule = userModule;
    }

    @Override
    public String toString() {
        return "UserExam{" +
                "id=" + id +
                ", exam=" + exam +
                ", examAttempts=" + examAttempts +
                ", user=" + userModule +
                '}';
    }
}
*/