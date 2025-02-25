package ap.student.project.backend.dao;

import ap.student.project.backend.entity.ChatMember;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMemberRepository extends ListCrudRepository<ChatMember, Integer> {
}
