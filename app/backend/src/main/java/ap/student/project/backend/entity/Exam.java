package ap.student.project.backend.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "exam")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "passing_score")
    private int passingScore;
    @Column(name = "max_attempts")
    private int maxAttempts;
    @Column
    private int time;
    @Column(name = "question_amount")
    private int questionAmount;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "question")
    private List<Question> questions;

    public Exam() {
    }

    public Exam(int passingScore, int maxAttempts, int time, int questionAmount, List<Question> questions) {
        this.passingScore = passingScore;
        this.maxAttempts = maxAttempts;
        this.time = time;
        this.questionAmount = questionAmount;
        this.questions = questions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPassingScore() {
        return passingScore;
    }

    public void setPassingScore(int passingScore) {
        this.passingScore = passingScore;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getQuestionAmount() {
        return questionAmount;
    }

    public void setQuestionAmount(int questionAmount) {
        this.questionAmount = questionAmount;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "Exam{" +
                "id=" + id +
                ", passingScore=" + passingScore +
                ", maxAttempts=" + maxAttempts +
                ", time=" + time +
                ", questionAmount=" + questionAmount +
                ", questions=" + questions +
                '}';
    }
}
