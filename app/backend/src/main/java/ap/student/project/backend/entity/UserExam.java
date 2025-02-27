package ap.student.project.backend.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "user_exam")
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

    public UserExam() {
    }

    public UserExam(Exam exam, List<ExamAttempt> examAttempts) {
        this.exam = exam;
        this.examAttempts = examAttempts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public List<ExamAttempt> getExamAttempts() {
        return examAttempts;
    }

    public void setExamAttempts(List<ExamAttempt> examAttempts) {
        this.examAttempts = examAttempts;
    }

    @Override
    public String toString() {
        return "UserExam{" +
                "id=" + id +
                ", exam=" + exam +
                ", examAttempts=" + examAttempts +
                '}';
    }
}
