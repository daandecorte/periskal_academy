package ap.student.project.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "date_time")
    private LocalDateTime dateTime;
    @Column(name = "text_content")
    private String textContent;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chat_id")
    private Chat chat;

    public Message(LocalDateTime dateTime, String textContent, Chat chat) {
        this.dateTime = dateTime;
        this.textContent = textContent;
        this.chat = chat;
    }
}
