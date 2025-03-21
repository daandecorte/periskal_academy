package ap.student.project.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "training_progress")
@Getter
@Setter
@NoArgsConstructor
public class TrainingProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trainin_id")
    private Training training;
    @Column(name = "video_watched")
    private boolean videoWatched;
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    @OneToMany(mappedBy = "trainingProgress", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<TrainingQuestionAnswer> trainingQuestionAnswers;

    public TrainingProgress(Training training, boolean videoWatched, LocalDateTime completedAt, List<TrainingQuestionAnswer> trainingQuestionAnswers) {
        this.training = training;
        this.videoWatched = videoWatched;
        this.completedAt = completedAt;
        this.trainingQuestionAnswers = trainingQuestionAnswers;
    }

    @Override
    public String toString() {
        return "TrainingProgress{" +
                "id=" + id +
                ", training=" + training +
                ", videoWatched=" + videoWatched +
                ", completedAt=" + completedAt +
                ", trainingQuestionAnswers=" + trainingQuestionAnswers +
                '}';
    }
}
