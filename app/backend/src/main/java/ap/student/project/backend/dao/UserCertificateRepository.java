package ap.student.project.backend.dao;

import ap.student.project.backend.entity.UserCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCertificateRepository extends JpaRepository<UserCertificate, Integer> {
}
