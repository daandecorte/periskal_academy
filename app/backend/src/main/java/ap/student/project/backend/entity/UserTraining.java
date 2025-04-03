package ap.student.project.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "user_training")
@Getter
@Setter
@NoArgsConstructor
public class UserTraining {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne(mappedBy = "userTraining", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private TrainingProgress trainingProgress;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "training_id")
    private Training training;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "userTraining",fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<ExamAttempt> examAttempts;

    public UserTraining(TrainingProgress trainingProgress, Training training, User user, List<ExamAttempt> examAttempts) {
        this.trainingProgress = trainingProgress;
        this.training = training;
        this.user = user;
        this.examAttempts = examAttempts;
    }

    @Override
    public String toString() {
        return "UserTraining{" +
                "id=" + id +
                ", trainingProgress=" + trainingProgress +
                ", training=" + training +
                ", user=" + user +
                '}';
    }
}
