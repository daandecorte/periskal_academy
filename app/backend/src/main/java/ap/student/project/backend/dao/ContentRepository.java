package ap.student.project.backend.dao;

import ap.student.project.backend.entity.Content;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository extends ListCrudRepository<Content, Integer> {
}
