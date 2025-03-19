package ap.student.project.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_module")
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

    public UserModule() {
    }

    public UserModule(ModuleProgress moduleProgress, Module module, User user) {
        this.moduleProgress = moduleProgress;
        this.module = module;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ModuleProgress getModuleProgress() {
        return moduleProgress;
    }

    public void setModuleProgress(ModuleProgress moduleProgress) {
        this.moduleProgress = moduleProgress;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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
