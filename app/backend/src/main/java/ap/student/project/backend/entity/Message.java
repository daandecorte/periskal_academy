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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chat_member_id")
    private ChatMember chatMember;

    public Message(LocalDateTime dateTime, String textContent, Chat chat, ChatMember chatMember) {
        this.dateTime = dateTime;
        this.textContent = textContent;
        this.chat = chat;
        this.chatMember = chatMember;
    }
}
