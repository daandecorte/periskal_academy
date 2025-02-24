package ap.student.project.backend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="message")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="date_time")
    private LocalDateTime dateTime;
    @Column(name="text_content")
    private String textContent;

    public Message() {}

    public Message(LocalDateTime dateTime, String textContent) {
        this.dateTime = dateTime;
        this.textContent = textContent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
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
