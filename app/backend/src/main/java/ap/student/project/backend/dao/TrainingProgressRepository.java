package ap.student.project.backend.dao;

import ap.student.project.backend.entity.TrainingProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface TrainingProgressRepository extends JpaRepository<TrainingProgress, Integer> {
    TrainingProgress findByUserTrainingId(int userTrainingId);
    
    @Modifying
    @Query("DELETE FROM TrainingProgress tp WHERE tp.userTraining.id = :userTrainingId")
    void deleteByUserTrainingId(@Param("userTrainingId") int userTrainingId);
}