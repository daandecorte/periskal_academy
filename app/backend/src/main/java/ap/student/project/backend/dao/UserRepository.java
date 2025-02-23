package ap.student.project.backend.dao;

import ap.student.project.backend.entity.User;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(collectionResourceRel = "user", path = "user")
public interface UserRepository extends ListCrudRepository<User, Long> {
}
