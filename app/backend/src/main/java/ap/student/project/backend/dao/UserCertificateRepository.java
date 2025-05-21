package ap.student.project.backend.dao;

import ap.student.project.backend.entity.UserCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCertificateRepository extends JpaRepository<UserCertificate, Integer> {
    Optional<UserCertificate> findByUserIdAndCertificateId(Integer userId, Integer certificateId);
}
