package ap.student.project.backend.entity;

import jakarta.persistence.*;

import java.util.Map;

@Entity
@Table(name="tip")
public class Tip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ElementCollection
    @CollectionTable(name = "tip_texts", joinColumns = @JoinColumn(name = "tip_id"))
    @MapKeyColumn(name = "language")
    @Column(name = "text")
    private Map<Language, String> text;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="module_id")
    private Module module;

    public Tip() {}

    public Tip(Map<Language, String> text, Module module) {
        this.text = text;
        this.module = module;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<Language, String> getText() {
        return text;
    }

    public void setText(Map<Language, String> text) {
        this.text = text;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    @Override
    public String toString() {
        return "Tip{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", module=" + module +
                '}';
    }
}
