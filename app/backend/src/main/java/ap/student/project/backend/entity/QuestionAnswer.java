package ap.student.project.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@ToString
public class QuestionAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id")
    private Question question;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_option_id")
    private QuestionOption questionOption;

    public QuestionAnswer(Question question, QuestionOption questionOption) {
        this.question = question;
        this.questionOption = questionOption;
    }
}
