package ap.student.project.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Entity
@Table(name = "training")
@Getter
@Setter
@NoArgsConstructor
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ElementCollection
    @CollectionTable(name = "training_titles", joinColumns = @JoinColumn(name = "training_id"))
    @MapKeyColumn(name = "language")
    @Column(name = "title")
    private Map<Language, String> title;
    @ElementCollection
    @CollectionTable(name = "training_descriptions", joinColumns = @JoinColumn(name = "training_id"))
    @MapKeyColumn(name = "language")
    @Column(name="description")
    private Map<Language, String> description;
    @Column(name = "is_active")
    private boolean isActive = false;
    @OneToMany(mappedBy = "training", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Module> modules;
    @OneToOne(mappedBy = "training", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Exam exam;
    @OneToMany(mappedBy = "training", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<Certificate> certificates;

    public Training(Map<Language, String> title, Map<Language, String> description, boolean isActive, List<Module> modules, Exam exam) {
        this.title = title;
        this.description = description;
        this.isActive = isActive;
        this.modules = modules;
        this.exam = exam;
    }

    @Override
    public String toString() {
        return "Training{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", isActive=" + isActive +
                ", modules=" + modules +
                ", exams=" + exam +
                '}';
    }
}
