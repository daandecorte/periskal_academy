package ap.student.project.backend.entity;

import jakarta.persistence.*;

import java.util.List;
import java.util.Map;

@Entity
@Table(name = "question")
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
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "question_option")
    private List<QuestionOption> questionOptions;

    public Question() {
    }

    public Question(Map<Language, String> text, QuestionType questionType, List<QuestionOption> questionOptions) {
        this.text = text;
        this.questionType = questionType;
        this.questionOptions = questionOptions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<Language, String> getText() {
        return text;
    }

    public void setText(Map<Language, String> text) {
        this.text = text;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public List<QuestionOption> getQuestionOptions() {
        return questionOptions;
    }

    public void setQuestionOptions(List<QuestionOption> questionOptions) {
        this.questionOptions = questionOptions;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", questionType=" + questionType +
                ", questionOptions=" + questionOptions +
                '}';
    }
}
