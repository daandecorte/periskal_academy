package ap.student.project.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "module_progress")
@Getter
@Setter
@NoArgsConstructor
public class ModuleProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "module_id")
    private Module module;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="training_progress_id")
    private TrainingProgress trainingProgress;
    @Column(name = "video_watched")
    private boolean videoWatched;
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    @OneToMany(mappedBy = "moduleProgress", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<ModuleQuestionAnswer> moduleQuestionAnswers;

    public ModuleProgress(Module module, boolean videoWatched, LocalDateTime completedAt, List<ModuleQuestionAnswer> moduleQuestionAnswers) {
        this.module = module;
        this.videoWatched = videoWatched;
        this.completedAt = completedAt;
        this.moduleQuestionAnswers = moduleQuestionAnswers;
    }

    @Override
    public String toString() {
        return "ModuleProgress{" +
                "id=" + id +
                ", module=" + module +
                ", videoWatched=" + videoWatched +
                ", completedAt=" + completedAt +
                ", moduleQuestionAnswers=" + moduleQuestionAnswers +
                '}';
    }
}
