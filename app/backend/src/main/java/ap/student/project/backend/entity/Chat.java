package ap.student.project.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Entity
@Table(name = "chat")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToMany(mappedBy = "chat", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Set<ChatMember> chatMembers;
    @Enumerated(EnumType.STRING)
    @Column(name = "chat_status")
    private ChatStatus chatStatus;

    public Chat(Set<ChatMember> chatMembers, ChatStatus chatStatus) {
        this.chatMembers = chatMembers;
        this.chatStatus = chatStatus;
    }
}
