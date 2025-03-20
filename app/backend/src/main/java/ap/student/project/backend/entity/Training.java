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
    @Column(name = "description")
    private Map<Language, String> description;
    @Enumerated(EnumType.STRING)
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "video_id")
    private Video video;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "module_id")
    @JsonIgnore
    private Module module;
    @OneToMany(mappedBy = "training", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Question> questions;

    public Training(Map<Language, String> title, Map<Language, String> description, Video video, Module module) {
        this.title = title;
        this.description = description;
        this.video = video;
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
