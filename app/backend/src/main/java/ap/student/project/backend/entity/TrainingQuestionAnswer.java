package ap.student.project.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "training_question_answer")
@Getter
@Setter
@NoArgsConstructor
public class TrainingQuestionAnswer extends QuestionAnswer {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "training_progress_id")
    private TrainingProgress trainingProgress;

    public TrainingQuestionAnswer(Question question, QuestionOption questionOption, TrainingProgress trainingProgress ) {
        super(question, questionOption);
        this.trainingProgress = trainingProgress;
    }
}
