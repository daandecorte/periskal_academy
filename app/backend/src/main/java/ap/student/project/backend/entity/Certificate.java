package ap.student.project.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "certificate")
@Getter
@Setter
@NoArgsConstructor
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "module_id")
    private Module module;
    @Column(name = "issue_date")
    private LocalDateTime issueDate;
    @Column(name = "expiry_date")
    private LocalDateTime expiryDate;
    @Column(name = "validity_period")
    private int validityPeriod;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CertificateStatus status;
    @Column(name = "price")
    private double price;

    public Certificate(Module module, LocalDateTime issueDate, LocalDateTime expiryDate, int validityPeriod, CertificateStatus status, double price) {
        this.module = module;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.validityPeriod = validityPeriod;
        this.status = status;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Certificate{" +
                "id=" + id +
                ", module=" + module +
                ", issueDate=" + issueDate +
                ", expiryDate=" + expiryDate +
                ", validityPeriod=" + validityPeriod +
                ", status=" + status +
                ", price=" + price +
                '}';
    }
}
