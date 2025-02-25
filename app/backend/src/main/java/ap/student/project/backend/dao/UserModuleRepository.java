package ap.student.project.backend.dao;

import ap.student.project.backend.entity.UserModule;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserModuleRepository extends ListCrudRepository<UserModule, Integer> {
}
