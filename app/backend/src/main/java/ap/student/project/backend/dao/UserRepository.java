package ap.student.project.backend.dao;

import ap.student.project.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByPeriskalId(String periskalId);
    boolean existsByPeriskalId(String periskalId);
}
