package ap.student.project.backend.entity;

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
    @Column(name = "video_reference")
    private String videoReference;
    @Column(name="language")
    private Language language;

    public Video(String videoReference, Language language) {
        this.videoReference = videoReference;
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
