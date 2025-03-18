package ap.student.project.backend.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "module")
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
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "training")
    private List<Training> trainings;
    @OneToMany(mappedBy = "module", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<Exam> exams;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name="tip")
    private List<Tip> tips;

    public Module() {
    }

    public Module(Map<Language, String> title, Map<Language, String> description, boolean isActive, List<Training> trainings, List<Exam> exams) {
        this.title = title;
        this.description = description;
        this.isActive = isActive;
        this.trainings = trainings;
        this.exams = exams;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<Language, String> getTitle() {
        return title;
    }

    public void setTitle(Map<Language, String> title) {
        this.title = title;
    }

    public Map<Language, String> getDescription() {
        return description;
    }

    public void setDescription(Map<Language, String> description) {
        this.description = description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<Training> getTrainings() {
        return trainings;
    }

    public void setTrainings(List<Training> trainings) {
        this.trainings = trainings;
    }

    public List<Exam> getExams() {
        return exams;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }

    @Nullable
    public List<Tip> getTips() {
        return tips;
    }

    public void setTips(@Nullable List<Tip> tips) {
        this.tips = tips;
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
