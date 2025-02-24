package ap.student.project.backend.dao;

import ap.student.project.backend.entity.User;
import ap.student.project.backend.entity.UserExam;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserExamRepository extends ListCrudRepository<UserExam, Integer> {
}
