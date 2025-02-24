package ap.student.project.backend.dao;

import ap.student.project.backend.entity.Chat;
import ap.student.project.backend.entity.Module;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRepository extends ListCrudRepository<Module, Integer> {
}
