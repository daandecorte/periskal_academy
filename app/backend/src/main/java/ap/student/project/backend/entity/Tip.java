package ap.student.project.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Entity
@Table(name="tip")
@Getter
@Setter
@NoArgsConstructor
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

    public Tip(Map<Language, String> text, Module module) {
        this.text = text;
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
