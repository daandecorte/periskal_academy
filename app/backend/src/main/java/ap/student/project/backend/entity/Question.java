package ap.student.project.backend.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private Language language;
    @Column
    private String text;
    @Enumerated(EnumType.STRING)
    @Column(name = "question_type")
    private QuestionType questionType;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "question_option")
    private List<QuestionOption> questionOptions;

    public Question() {
    }

    public Question(Language language, String text, QuestionType questionType, List<QuestionOption> questionOptions) {
        this.language = language;
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

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
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
                ", language=" + language +
                ", text='" + text + '\'' +
                ", questionType=" + questionType +
                ", questionOptions=" + questionOptions +
                '}';
    }
}
