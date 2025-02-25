package ap.student.project.backend.dao;

import ap.student.project.backend.entity.Message;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends ListCrudRepository<Message, Integer> {
}
