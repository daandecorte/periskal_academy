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

    public Video() {
    }

    public Video(String videoReference) {
        this.videoReference = videoReference;
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

    @Override
    public String toString() {
        return "Video{" +
                "id=" + id +
                ", videoReference='" + videoReference + '\'' +
                '}';
    }
}
