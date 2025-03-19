package ap.student.project.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Map;

@Entity
@Table(name = "training")
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
    @Column(name = "description")
    private Map<Language, String> description;
    @Enumerated(EnumType.STRING)
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "video_id")
    private Video video;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "module_id")
    @JsonIgnore
    private Module module;

    public Training() {
    }

    public Training(Map<Language, String> title, Map<Language, String> description, Video video, Module module) {
        this.title = title;
        this.description = description;
        this.video = video;
        this.module = module;
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

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    @Override
    public String toString() {
        return "Training{" +
                "id=" + id +
                ", title=" + title +
                ", description=" + description +
                ", video=" + video +
                ", module=" + module +
                '}';
    }
}
