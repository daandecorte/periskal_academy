package ap.student.project.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "chat")
@Getter
@Setter
@NoArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "chatmember_id")
    private Set<ChatMember> chatMembers;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "message_id")
    private Set<Message> messages;
    @Enumerated(EnumType.STRING)
    @Column(name = "chat_status")
    private ChatStatus chatStatus;

    public Chat(Set<ChatMember> chatMembers, Set<Message> messages, ChatStatus chatStatus) {
        this.chatMembers = chatMembers;
        this.messages = messages;
        this.chatStatus = chatStatus;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "id=" + id +
                ", chatMembers=" + chatMembers +
                ", messages=" + messages +
                ", chatStatus=" + chatStatus +
                '}';
    }
}
