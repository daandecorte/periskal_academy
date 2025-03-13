package ap.student.project.backend.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "module")
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private Language language;
    @Column(name = "is_active")
    private boolean isActive = false;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "training")
    private List<Training> trainings;
    @OneToMany
    @JoinTable(name = "exam")
    private List<Exam> exams;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name="tip")
    @Nullable
    private List<Tip> tips;

    public Module() {
    }

    public Module(String title, String description, Language language, boolean isActive, List<Training> trainings, List<Exam> exams) {
        this.title = title;
        this.description = description;
        this.language = language;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
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
                ", language=" + language +
                ", isActive=" + isActive +
                ", trainings=" + trainings +
                ", exams=" + exams +
                ", tips=" + tips +
                '}';
    }
}
