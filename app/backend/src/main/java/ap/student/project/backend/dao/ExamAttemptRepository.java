package ap.student.project.backend.dao;

import ap.student.project.backend.entity.ExamAttempt;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@RepositoryRestResource(exported = false)
public interface ExamAttemptRepository extends ListCrudRepository<ExamAttempt, Integer> {
}
