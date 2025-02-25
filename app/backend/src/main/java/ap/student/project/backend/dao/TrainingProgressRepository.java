package ap.student.project.backend.dao;

import ap.student.project.backend.entity.TrainingProgress;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingProgressRepository extends ListCrudRepository<TrainingProgress, Integer> {
}
