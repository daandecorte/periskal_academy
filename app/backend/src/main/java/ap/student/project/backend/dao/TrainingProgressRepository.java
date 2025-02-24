package ap.student.project.backend.dao;

import ap.student.project.backend.entity.TrainingProgress;
import ap.student.project.backend.entity.User;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingProgressRepository extends ListCrudRepository<TrainingProgress, Integer> {
}
