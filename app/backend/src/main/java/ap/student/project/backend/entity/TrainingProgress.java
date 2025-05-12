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
    @OneToMany( mappedBy = "trainingProgress", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<ModuleProgress> moduleProgresses;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_training_id")
    @JsonIgnore
    private UserTraining userTraining;

    public TrainingProgress(LocalDateTime startDateTime, LocalDateTime lastTimeAccessed, ProgressStatusType status, List<ModuleProgress> moduleProgresses, UserTraining userTraining) {
        this.startDateTime = startDateTime;
        this.lastTimeAccessed = lastTimeAccessed;
        this.status = status;
        this.moduleProgresses = moduleProgresses;
        this.userTraining = userTraining;
    }

    @Override
    public String toString() {
        return "TrainingProgress{" +
                "id=" + id +
                ", startDateTime=" + startDateTime +
                ", lastTimeAccessed=" + lastTimeAccessed +
                ", status=" + status +
                ", moduleProgresses=" + moduleProgresses +
                ", userTraining=" + userTraining +
                '}';
    }
}
