package ap.student.project.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @CollectionTable(name = "module_titles", joinColumns = @JoinColumn(name = "training_id", referencedColumnName = "id"))
    @MapKeyColumn(name = "language")
    @Column(name = "title")
    private Map<Language, String> title = new HashMap<>();
    @ElementCollection
    @CollectionTable(name = "module_descriptions", joinColumns = @JoinColumn(name = "training_id"))
    @MapKeyColumn(name = "language")
    @Column(name = "description")
    private Map<Language, String> description = new HashMap<>();
    @ElementCollection
    @CollectionTable(name = "video_references", joinColumns = @JoinColumn(name = "training_id"))
    @MapKeyColumn(name = "language")
    @Column(name = "video_reference")
    private Map<Language, String> videoReference = new HashMap<>();
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "training_id")
    @JsonIgnore
    private Training training;
    @OneToMany(mappedBy = "module", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Question> questions;

    public Module(Map<Language, String> title, Map<Language, String> description, Map<Language, String> videoReference, Training training, List<Question> questions) {
        this.title = title != null ? title : new HashMap<>();
        this.description = description != null ? description : new HashMap<>();
        this.videoReference = videoReference != null ? videoReference : new HashMap<>();
        this.training = training;
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "Module{" +
                "id=" + id +
                ", title=" + title +
                ", description=" + description +
                ", videoReference=" + videoReference +
                ", training=" + training +
                ", questions=" + questions +
                '}';
    }
}