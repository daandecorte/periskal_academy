package ap.student.project.backend.dao;

import ap.student.project.backend.entity.UserTraining;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface UserTrainingRepository extends JpaRepository<UserTraining, Integer> {
    Optional<UserTraining> findByTrainingIdAndUserId(int trainingId, int userId);
}
