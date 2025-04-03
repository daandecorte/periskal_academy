package ap.student.project.backend.dao;

import ap.student.project.backend.entity.UserTraining;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface UserTrainingRepository extends ListCrudRepository<UserTraining, Integer> {
}
