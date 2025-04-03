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
public class ModuleQuestionAnswer extends QuestionAnswer {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "module_progress_id")
    private ModuleProgress moduleProgress;

    public ModuleQuestionAnswer(Question question, QuestionOption questionOption, ModuleProgress moduleProgress) {
        super(question, questionOption);
        this.moduleProgress = moduleProgress;
    }
}
