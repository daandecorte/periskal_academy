package ap.student.project.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @JoinColumn(name = "training_id")
    private Training training;
    @Column(name = "validity_period")
    private int validityPeriod;
    @Column(name = "price")
    private double price;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Certificate(Training training, int validityPeriod, double price, User user) {
        this.training = training;
        this.validityPeriod = validityPeriod;
        this.price = price;
        this.user = user;
    }

    @Override
    public String toString() {
        return "Certificate{" +
                "id=" + id +
                ", training=" + training +
                ", validityPeriod=" + validityPeriod +
                ", price=" + price +
                ", user=" + user +
                '}';
    }
}
