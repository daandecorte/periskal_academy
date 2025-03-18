package ap.student.project.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Column(name = "user_id")
    private String userId;
    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private Language language;
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<UserExam> userExams;
    @OneToMany( mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<UserModule> userModules;

    public User() {

    }

    public User(String userId, Language language) {
        this.userId = userId;
        this.language = language;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
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
                ", userId='" + userId + '\'' +
                ", language=" + language +
                ", userExams=" + userExams +
                ", userModules=" + userModules +
                '}';
    }
}
