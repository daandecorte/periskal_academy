package ap.student.project.backend.dao;

import ap.student.project.backend.entity.Message;
import ap.student.project.backend.entity.User;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends ListCrudRepository<Message, Integer> {
}
