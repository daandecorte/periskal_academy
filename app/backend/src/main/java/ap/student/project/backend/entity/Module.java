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
    private Map<Language, String> description;
    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Content> content;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "training_id")
    @JsonIgnore
    private Training training;
    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions;

    public Module(Map<Language, String> title, Map<Language, String> description, List<Content> content, Training training, List<Question> questions) {
        this.title = title;
        this.description = description;
        this.content = content;
        this.training = training;
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "Module{" +
                "id=" + id +
                ", title=" + title +
                ", description=" + description +
                ", references=" + content +
                ", training=" + training +
                ", questions=" + questions +
                '}';
    }
}