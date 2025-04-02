package ap.student.project.backend.dao;

import ap.student.project.backend.entity.User;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface UserRepository extends ListCrudRepository<User, Integer> {
    User findByPeriskalId(String periskalId);
    boolean existsByPeriskalId(String periskalId);
}
