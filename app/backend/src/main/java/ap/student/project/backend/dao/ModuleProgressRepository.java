package ap.student.project.backend.dao;

import ap.student.project.backend.entity.ModuleProgress;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface ModuleProgressRepository extends ListCrudRepository<ModuleProgress, Integer> {
}
