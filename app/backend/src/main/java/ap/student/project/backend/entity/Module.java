package ap.student.project.backend.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "module")
@Getter
@Setter
@NoArgsConstructor
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ElementCollection
    @CollectionTable(name = "module_titles", joinColumns = @JoinColumn(name = "module_id"))
    @MapKeyColumn(name = "language")
    @Column(name = "title")
    private Map<Language, String> title;
    @ElementCollection
    @CollectionTable(name = "module_descriptions", joinColumns = @JoinColumn(name = "module_id"))
    @MapKeyColumn(name = "language")
    @Column(name="description")
    private Map<Language, String> description;
    @Column(name = "is_active")
    private boolean isActive = false;
    @OneToMany(mappedBy = "module", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Training> trainings;
    @OneToMany(mappedBy = "module", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Exam> exams;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name="tip")
    private List<Tip> tips;

    public Module(Map<Language, String> title, Map<Language, String> description, boolean isActive, List<Training> trainings, List<Exam> exams) {
        this.title = title;
        this.description = description;
        this.isActive = isActive;
        this.trainings = trainings;
        this.exams = exams;
    }

    @Override
    public String toString() {
        return "Module{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", isActive=" + isActive +
                ", trainings=" + trainings +
                ", exams=" + exams +
                ", tips=" + tips +
                '}';
    }
}
