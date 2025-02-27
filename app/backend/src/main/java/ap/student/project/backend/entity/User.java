package ap.student.project.backend.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "dongle_id")
    private String dongleId;
    @Column(name = "fleet_manager_id")
    private int fleetManagerId;
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;
    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private Language language;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_exam")
    @Nullable
    private List<UserExam> userExams;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_module")
    @Nullable
    private List<UserModule> userModules;

    public User() {

    }

    public User(String dongleId, int fleetManagerId, String name, String email, Role role, Language language, List<UserExam> userExams, List<UserModule> userModules) {
        this.dongleId = dongleId;
        this.fleetManagerId = fleetManagerId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.language = language;
        this.userExams = userExams;
        this.userModules = userModules;
    }

    public String toJSON() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDongleId() {
        return dongleId;
    }

    public void setDongleId(String dongleId) {
        this.dongleId = dongleId;
    }

    public int getFleetManagerId() {
        return fleetManagerId;
    }

    public void setFleetManagerId(int fleetManagerId) {
        this.fleetManagerId = fleetManagerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Nullable
    public List<UserExam> getUserExams() {
        return userExams;
    }

    public void setUserExams(@Nullable List<UserExam> userExams) {
        this.userExams = userExams;
    }

    @Nullable
    public List<UserModule> getUserModules() {
        return userModules;
    }

    public void setUserModules(@Nullable List<UserModule> userModules) {
        this.userModules = userModules;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", dongleId='" + dongleId + '\'' +
                ", fleetManagerId=" + fleetManagerId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", language=" + language +
                ", userExams=" + userExams +
                ", userModules=" + userModules +
                '}';
    }
}
