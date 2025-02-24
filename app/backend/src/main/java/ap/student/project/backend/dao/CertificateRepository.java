package ap.student.project.backend.dao;

import ap.student.project.backend.entity.Certificate;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRepository extends ListCrudRepository<Certificate, Integer> {
}
