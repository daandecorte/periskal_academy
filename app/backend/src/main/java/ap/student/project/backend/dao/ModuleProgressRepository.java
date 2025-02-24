package ap.student.project.backend.dao;

import ap.student.project.backend.entity.ModuleProgress;
import ap.student.project.backend.entity.User;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleProgressRepository extends ListCrudRepository<ModuleProgress, Integer> {
}
