package ap.student.project.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Entity
@Table(name = "content")
@Getter
@Setter
@NoArgsConstructor
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "content_type")
    @Enumerated(EnumType.STRING)
    private ContentType contentType;
    @ElementCollection
    @CollectionTable(name = "content_references", joinColumns = @JoinColumn(name = "content_id", referencedColumnName = "id"))
    @MapKeyColumn(name = "language")
    @Column(name = "reference")
    private Map<Language, String> reference;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "module_id")
    @JsonIgnore
    private Module module;

    public Content(ContentType contentType, Map<Language, String> reference, Module module) {
        this.contentType = contentType;
        this.reference = reference;
        this.module = module;
    }

    @Override
    public String toString() {
        return "Content{" +
                "contentType=" + contentType +
                ", reference=" + reference +
                ", module=" + module + '}';
    }
}
