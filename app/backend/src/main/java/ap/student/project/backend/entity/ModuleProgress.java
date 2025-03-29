package ap.student.project.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @OneToMany( mappedBy = "moduleProgress", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<TrainingProgress> trainingProgress;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="user_module_id")
    @JsonIgnore
    private UserModule userModule;

    public ModuleProgress(LocalDateTime startDateTime, LocalDateTime lastTimeAccessed, ProgressStatusType status, List<TrainingProgress> trainingProgress, UserModule userModule) {
        this.startDateTime = startDateTime;
        this.lastTimeAccessed = lastTimeAccessed;
        this.status = status;
        this.trainingProgress = trainingProgress;
        this.userModule = userModule;
    }

    @Override
    public String toString() {
        return "ModuleProgress{" +
                "id=" + id +
                ", startDateTime=" + startDateTime +
                ", lastTimeAccessed=" + lastTimeAccessed +
                ", status=" + status +
                ", trainingProgress=" + trainingProgress +
                ", userModule=" + userModule +
                '}';
    }
}
