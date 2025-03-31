/*package ap.student.project.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "video")
@Getter
@Setter
@NoArgsConstructor
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "video_references")
    private Map<Language, String> videoReferences;

    public Video(Map<Language, String> videoReferences) {
        this.videoReferences = videoReference;
        this.language = language;
    }

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", videoReference='" + videoReference + '\'' +
                ", language=" + language +
                '}';
    }
}
*/