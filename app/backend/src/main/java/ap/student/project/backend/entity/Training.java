package ap.student.project.backend.entity;

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
    @OneToMany(mappedBy = "training", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Exam> exams;

    public Training(Map<Language, String> title, Map<Language, String> description, boolean isActive, List<Module> modules, List<Exam> exams) {
        this.title = title;
        this.description = description;
        this.isActive = isActive;
        this.modules = modules;
        this.exams = exams;
    }

    @Override
    public String toString() {
        return "Training{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", isActive=" + isActive +
                ", modules=" + modules +
                ", exams=" + exams +
                '}';
    }
}
