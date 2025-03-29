package ap.student.project.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "user_module")
@Getter
@Setter
@NoArgsConstructor
public class UserModule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne(mappedBy = "userModule", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private ModuleProgress moduleProgress;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "module_id")
    private Module module;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany(mappedBy = "userModule",fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<ExamAttempt> examAttempts;

    public UserModule(ModuleProgress moduleProgress, Module module, User user, List<ExamAttempt> examAttempts) {
        this.moduleProgress = moduleProgress;
        this.module = module;
        this.user = user;
        this.examAttempts = examAttempts;
    }

    @Override
    public String toString() {
        return "UserModule{" +
                "id=" + id +
                ", moduleProgress=" + moduleProgress +
                ", module=" + module +
                ", user=" + user +
                '}';
    }
}
