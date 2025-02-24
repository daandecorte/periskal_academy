package ap.student.project.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "certificate")
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="module_id")
    private Module module;
    @Column(name="issue_date")
    private LocalDateTime issueDate;
    @Column(name="expiry_date")
    private LocalDateTime expiryDate;
    @Column(name="validity_period")
    private int validityPeriod;
    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private CertificateStatus status;
    @Column(name="price")
    private double price;

    public Certificate() {}

    public Certificate(Module module, LocalDateTime issueDate, LocalDateTime expiryDate, int validityPeriod, CertificateStatus status, double price) {
        this.module = module;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.validityPeriod = validityPeriod;
        this.status = status;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(int validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public CertificateStatus getStatus() {
        return status;
    }

    public void setStatus(CertificateStatus status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
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
