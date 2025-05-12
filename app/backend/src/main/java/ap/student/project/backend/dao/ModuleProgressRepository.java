package ap.student.project.backend.dao;

import ap.student.project.backend.entity.ModuleProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface ModuleProgressRepository extends JpaRepository<ModuleProgress, Integer> {
}
