package ap.student.project.backend.repository;

import ap.student.project.backend.entity.TestEntity;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends ListCrudRepository<TestEntity, Long> {
}
