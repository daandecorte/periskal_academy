package ap.student.project.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chat_member")
@Getter
@Setter
@NoArgsConstructor
public class ChatMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public ChatMember(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "ChatMember{" +
                "id=" + id +
                ", user=" + user +
                '}';
    }
}
