package ap.student.project.backend.dao;

import ap.student.project.backend.entity.QuestionOption;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionOptionRepository extends ListCrudRepository<QuestionOption, Integer> {
}
