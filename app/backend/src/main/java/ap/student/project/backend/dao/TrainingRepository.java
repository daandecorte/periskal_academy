package ap.student.project.backend.dao;

import ap.student.project.backend.entity.Chat;
import ap.student.project.backend.entity.Training;
import ch.qos.logback.core.rolling.helper.IntegerTokenConverter;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingRepository extends ListCrudRepository<Training, Integer> {
}
