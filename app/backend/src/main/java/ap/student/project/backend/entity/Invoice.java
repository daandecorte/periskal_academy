package ap.student.project.backend.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.ManyToAny;

@Entity
@Table(name = "invoice")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
}
