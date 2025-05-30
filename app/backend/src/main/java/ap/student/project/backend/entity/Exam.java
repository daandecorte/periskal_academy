package ap.student.project.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "exam")
@Getter
@Setter
@NoArgsConstructor
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
    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "training_id")
    @JsonIgnore
    private Training training;

    public Exam(int passingScore, int maxAttempts, int time, int questionAmount, List<Question> questions, Training training) {
        this.passingScore = passingScore;
        this.maxAttempts = maxAttempts;
        this.time = time;
        this.questionAmount = questionAmount;
        this.questions = questions;
        this.training = training;
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
                ", training=" + training +
                '}';
    }
}
