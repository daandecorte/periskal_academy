package ap.student.project.backend.dao;

import ap.student.project.backend.entity.ModuleProgress;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleProgressRepository extends ListCrudRepository<ModuleProgress, Integer> {
}
