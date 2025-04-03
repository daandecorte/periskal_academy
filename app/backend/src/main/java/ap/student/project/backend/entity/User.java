package ap.student.project.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "periskal_id")
    private String periskalId;
    @Column(name="firstname")
    private String firstname;
    @Column(name="lastname")
    private String lastname;
    @Column(name="shipname")
    private String shipname;
    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private Language language;
    @OneToMany( mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<UserTraining> userTrainings;
    @OneToMany( mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<ChatMember> chatMembers;

    public User(String periskalId, String firstname, String lastname, String shipname, Language language) {
        this.periskalId = periskalId;
        this.language = language;
        this.firstname = firstname;
        this.lastname = lastname;
        this.shipname = shipname;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userId='" + periskalId + '\'' +
                ", language=" + language +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", shipname='" + shipname + '\'' +
                ", userTrainings=" + userTrainings +
                '}';
    }
}
