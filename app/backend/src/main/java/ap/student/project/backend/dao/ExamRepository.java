package ap.student.project.backend.dao;

import ap.student.project.backend.entity.Exam;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends ListCrudRepository<Exam, Integer> {
}
