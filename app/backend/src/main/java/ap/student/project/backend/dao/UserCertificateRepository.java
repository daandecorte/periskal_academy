package ap.student.project.backend.dao;

import ap.student.project.backend.entity.UserCertificate;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCertificateRepository extends ListCrudRepository<UserCertificate, Integer> {
}
