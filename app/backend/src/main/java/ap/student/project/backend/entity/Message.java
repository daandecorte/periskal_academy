package ap.student.project.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@Getter
@Setter
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "date_time")
    private LocalDateTime dateTime;
    @Column(name = "text_content")
    private String textContent;

    public Message(LocalDateTime dateTime, String textContent) {
        this.dateTime = dateTime;
        this.textContent = textContent;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", textContent='" + textContent + '\'' +
                '}';
    }
}
