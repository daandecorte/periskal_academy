package ap.student.project.backend.dao;

import ap.student.project.backend.entity.ExamAttempt;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface ExamAttemptRepository extends JpaRepository<ExamAttempt, Integer> {
    // Count failed attempts for a specific user training
    @Query("SELECT COUNT(ea) FROM ExamAttempt ea WHERE ea.userTraining.id = :userTrainingId AND ea.examStatusType = 'FAILED'")
    int countFailedAttemptsByUserTrainingId(@Param("userTrainingId") int userTrainingId);
    
    // Find all failed attempts for a specific user training
    @Query("SELECT ea FROM ExamAttempt ea WHERE ea.userTraining.id = :userTrainingId AND ea.examStatusType = 'FAILED'")
    List<ExamAttempt> findFailedAttemptsByUserTrainingId(@Param("userTrainingId") int userTrainingId);
}
