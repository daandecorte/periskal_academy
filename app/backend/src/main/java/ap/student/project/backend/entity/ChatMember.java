package ap.student.project.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chat_member")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ChatMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="chat_id")
    @JsonIgnore
    private Chat chat;
    @OneToMany(mappedBy = "chatMember", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private List<Message> messages = new ArrayList<>();


    public ChatMember(User user, Chat chat, List<Message> messages) {
        this.user = user;
        this.chat = chat;
        this.messages = messages;
    }
}
