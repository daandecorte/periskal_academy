package ap.student.project.backend.dao;

import ap.student.project.backend.entity.Tip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipRepository extends JpaRepository<Tip, Integer> {
}
