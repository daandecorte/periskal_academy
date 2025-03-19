package ap.student.project.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "video")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "video_reference")
    private String videoReference;
    @Column(name="language")
    private Language language;

    public Video() {
    }

    public Video(String videoReference, Language language) {
        this.videoReference = videoReference;
        this.language = language;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVideoReference() {
        return videoReference;
    }

    public void setVideoReference(String videoReference) {
        this.videoReference = videoReference;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
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
