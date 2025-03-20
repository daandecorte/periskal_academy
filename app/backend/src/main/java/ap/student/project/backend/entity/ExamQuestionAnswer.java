package ap.student.project.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "exam_question_answer")
@Getter
@Setter
@NoArgsConstructor
public class ExamQuestionAnswer extends QuestionAnswer {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exam_attempt_id")
    private ExamAttempt examAttempt;

    public ExamQuestionAnswer(Question question, QuestionOption questionOption, ExamAttempt examAttempt) {
        super(question, questionOption);
        this.examAttempt = examAttempt;
    }
}
