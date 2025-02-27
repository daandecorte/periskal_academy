package ap.student.project.backend.dao;

import ap.student.project.backend.entity.Question;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends ListCrudRepository<Question, Integer> {
}
