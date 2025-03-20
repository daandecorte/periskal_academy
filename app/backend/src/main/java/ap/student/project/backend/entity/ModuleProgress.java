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
    @Column(name = "start_date_time")
    private LocalDateTime startDateTime;
    @Column(name = "last_time_accessed")
    private LocalDateTime lastTimeAccessed;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProgressStatusType status;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "training_progress")
    private List<TrainingProgress> trainingProgress;

    public ModuleProgress(LocalDateTime startDateTime, LocalDateTime lastTimeAccessed, ProgressStatusType status, List<TrainingProgress> trainingProgress) {
        this.startDateTime = startDateTime;
        this.lastTimeAccessed = lastTimeAccessed;
        this.status = status;
        this.trainingProgress = trainingProgress;
    }

    @Override
    public String toString() {
        return "ModuleProgress{" +
                "id=" + id +
                ", startDateTime=" + startDateTime +
                ", lastTimeAccessed=" + lastTimeAccessed +
                ", status=" + status +
                ", trainingProgress=" + trainingProgress +
                '}';
    }
}
