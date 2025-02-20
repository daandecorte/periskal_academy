package ap.student.project.backend.entity;

import jakarta.persistence.*;

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
    @Column(name = "role")
    private Role role;
    @Column(name = "language")
    private Language language;

    public User() {

    }

    public User(String dongleId, int fleetManagerId, String name, String email, Role role, Language language) {
        this.dongleId = dongleId;
        this.fleetManagerId = fleetManagerId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.language = language;
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

    public Role getUserRole() {
        return role;
    }

    public void setUserRole(Role role) {
        this.role = role;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", dongleId='" + dongleId + '\'' +
                ", fleetManagerId=" + fleetManagerId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", userRole=" + role +
                ", language=" + language +
                '}';
    }
}
