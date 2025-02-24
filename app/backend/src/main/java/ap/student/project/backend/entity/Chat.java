package ap.student.project.backend.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name="chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name="chatmember_id")
    private Set<ChatMember> chatMembers;
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name="message_id")
    private Set<Message> messages;
    @Enumerated(EnumType.STRING)
    @Column(name="chat_status")
    private ChatStatus chatStatus;

    public Chat() {}

    public Chat(Set<ChatMember> chatMembers, Set<Message> messages, ChatStatus chatStatus) {
        this.chatMembers = chatMembers;
        this.messages = messages;
        this.chatStatus = chatStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<ChatMember> getChatMembers() {
        return chatMembers;
    }

    public void setChatMembers(Set<ChatMember> chatMembers) {
        this.chatMembers = chatMembers;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    public ChatStatus getChatStatus() {
        return chatStatus;
    }

    public void setChatStatus(ChatStatus chatStatus) {
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
