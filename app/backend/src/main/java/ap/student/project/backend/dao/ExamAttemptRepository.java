package ap.student.project.backend.dao;

import ap.student.project.backend.entity.ExamAttempt;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamAttemptRepository extends ListCrudRepository<ExamAttempt, Integer> {
}
