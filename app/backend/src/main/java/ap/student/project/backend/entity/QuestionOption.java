package ap.student.project.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "question_option")
public class QuestionOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String text;
    @Column(name = "is_correct")
    private boolean isCorrect;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id")
    private Question question;

    public QuestionOption() {
    }

    public QuestionOption(String text, boolean isCorrect, Question question) {
        this.text = text;
        this.isCorrect = isCorrect;
        this.question = question;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "QuestionOption{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", isCorrect=" + isCorrect +
                ", question=" + question +
                '}';
    }
}
