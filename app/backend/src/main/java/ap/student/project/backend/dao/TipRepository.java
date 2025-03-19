package ap.student.project.backend.dao;

import ap.student.project.backend.entity.Tip;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipRepository extends ListCrudRepository<Tip, Integer> {
}
