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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "topic_id")
    private Topic topic;
    @ElementCollection
    @CollectionTable(name = "tip_texts", joinColumns = @JoinColumn(name = "tip_id"))
    @MapKeyColumn(name = "language")
    @Column(name = "text")
    private Map<Language, String> text;

    public Tip(Topic topic,Map<Language, String> text) {
        this.topic = topic;
        this.text = text;
    }

    @Override
    public String toString() {
        return "Tip{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }
}
