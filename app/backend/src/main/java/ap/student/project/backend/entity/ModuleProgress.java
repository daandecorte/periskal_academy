package ap.student.project.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "module_progress")
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

    public ModuleProgress() {
    }

    public ModuleProgress(LocalDateTime startDateTime, LocalDateTime lastTimeAccessed, ProgressStatusType status, List<TrainingProgress> trainingProgress) {
        this.startDateTime = startDateTime;
        this.lastTimeAccessed = lastTimeAccessed;
        this.status = status;
        this.trainingProgress = trainingProgress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getLastTimeAccessed() {
        return lastTimeAccessed;
    }

    public void setLastTimeAccessed(LocalDateTime lastTimeAccessed) {
        this.lastTimeAccessed = lastTimeAccessed;
    }

    public ProgressStatusType getStatus() {
        return status;
    }

    public void setStatus(ProgressStatusType status) {
        this.status = status;
    }

    public List<TrainingProgress> getTrainingProgress() {
        return trainingProgress;
    }

    public void setTrainingProgress(List<TrainingProgress> trainingProgress) {
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
