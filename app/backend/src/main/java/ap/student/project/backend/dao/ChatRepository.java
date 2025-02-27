package ap.student.project.backend.dao;

import ap.student.project.backend.entity.Chat;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends ListCrudRepository<Chat, Integer> {
}
