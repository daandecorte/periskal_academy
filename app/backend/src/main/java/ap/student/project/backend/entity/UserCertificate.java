package ap.student.project.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "user_certificate")
@Getter
@Setter
@NoArgsConstructor
public class UserCertificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "issue_date")
    private LocalDate issueDate;
    @Column(name="expiry_date")
    private LocalDate expiryDate;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CertificateStatus status;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "certificate_id")
    private Certificate certificate;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private User user;

    public UserCertificate(LocalDate issueDate, LocalDate expiryDate, Certificate certificate, User user) {
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.certificate = certificate;
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserCertificate{" +
                "id=" + id +
                ", issueDate=" + issueDate +
                ", expiryDate=" + expiryDate +
                ", status=" + status +
                ", certificate=" + certificate +
                ", user=" + user +
                '}';
    }
}
