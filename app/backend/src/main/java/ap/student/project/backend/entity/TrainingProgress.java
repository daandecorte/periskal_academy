package ap.student.project.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class  TrainingProgress {
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
    @Column(name = "modules_completed")
    private int modulesCompleted;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_training_id")
    @JsonIgnore
    private UserTraining userTraining;

    public TrainingProgress(LocalDateTime startDateTime, LocalDateTime lastTimeAccessed, ProgressStatusType status, int modulesCompleted, UserTraining userTraining) {
        this.startDateTime = startDateTime;
        this.lastTimeAccessed = lastTimeAccessed;
        this.status = status;
        this.modulesCompleted = modulesCompleted;
        this.userTraining = userTraining;
    }

    @Override
    public String toString() {
        return "TrainingProgress{" +
                "id=" + id +
                ", startDateTime=" + startDateTime +
                ", lastTimeAccessed=" + lastTimeAccessed +
                ", status=" + status +
                ", modulesCompleted=" + modulesCompleted +
                ", userTraining=" + userTraining +
                '}';
    }
}
