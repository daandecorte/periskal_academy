package ap.student.project.backend.dao;

import ap.student.project.backend.entity.ChatMember;
import ap.student.project.backend.entity.User;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMemberRepository extends ListCrudRepository<ChatMember, Integer> {
}
