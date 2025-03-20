package ap.student.project.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_module")
@Getter
@Setter
@NoArgsConstructor
public class UserModule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "module_progress")
    private ModuleProgress moduleProgress;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "module_id")
    private Module module;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public UserModule(ModuleProgress moduleProgress, Module module, User user) {
        this.moduleProgress = moduleProgress;
        this.module = module;
        this.user = user;
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
