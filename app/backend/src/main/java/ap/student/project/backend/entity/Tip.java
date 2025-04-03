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
    @CollectionTable(name = "tip_titles", joinColumns = @JoinColumn(name = "tip_id"))
    @MapKeyColumn(name = "language")
    @Column(name = "title")
    private Map<Language, String> title;
    @ElementCollection
    @CollectionTable(name = "tip_texts", joinColumns = @JoinColumn(name = "tip_id"))
    @MapKeyColumn(name = "language")
    @Column(name = "text")
    private Map<Language, String> text;

    public Tip(Map<Language, String> title, Map<Language, String> text) {
        this.title = title;
        this.text = text;
    }

    @Override
    public String toString() {
        return "Tip{" +
                "id=" + id +
                ", title='" + title  + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
