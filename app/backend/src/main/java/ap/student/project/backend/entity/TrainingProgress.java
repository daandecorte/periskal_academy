package ap.student.project.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="training_progress")
public class TrainingProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="trainin_id")
    private Training training;
    @Column(name="video_watched")
    private boolean videoWatched;
    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public TrainingProgress() {}

    public TrainingProgress(Training training, boolean videoWatched, LocalDateTime completedAt) {
        this.training = training;
        this.videoWatched = videoWatched;
        this.completedAt = completedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Training getTraining() {
        return training;
    }

    public void setTraining(Training training) {
        this.training = training;
    }

    public boolean isVideoWatched() {
        return videoWatched;
    }

    public void setVideoWatched(boolean videoWatched) {
        this.videoWatched = videoWatched;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    @Override
    public String toString() {
        return "TrainingProgress{" +
                "id=" + id +
                ", training=" + training +
                ", videoWatched=" + videoWatched +
                ", completedAt=" + completedAt +
                '}';
    }
}
