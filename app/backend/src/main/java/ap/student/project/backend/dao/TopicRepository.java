package ap.student.project.backend.dao;

import ap.student.project.backend.entity.Topic;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends ListCrudRepository<Topic, Integer> {
}
