package ap.student.project.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "certificate")
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
}
