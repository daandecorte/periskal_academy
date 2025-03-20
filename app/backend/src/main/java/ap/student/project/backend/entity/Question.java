package ap.student.project.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Entity
@Table(name = "question")
@Getter
@Setter
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ElementCollection
    @CollectionTable(name = "question_texts", joinColumns = @JoinColumn(name = "question_id"))
    @MapKeyColumn(name = "language")
    @Column(name = "text")
    private Map<Language, String> text;
    @Enumerated(EnumType.STRING)
    @Column(name = "question_type")
    private QuestionType questionType;
    @OneToMany(mappedBy = "question", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<QuestionOption> questionOptions;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exam_id")
    @JsonIgnore
    private Exam exam;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exam_id")
    @JsonIgnore
    private Training training;

    public Question(Map<Language, String> text, QuestionType questionType, List<QuestionOption> questionOptions, Exam exam, Training training) {
        this.text = text;
        this.questionType = questionType;
        this.questionOptions = questionOptions;
        this.exam = exam;
        this.training = training;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", text=" + text +
                ", questionType=" + questionType +
                ", questionOptions=" + questionOptions +
                ", exam=" + exam +
                ", training=" + training +
                '}';
    }
}
