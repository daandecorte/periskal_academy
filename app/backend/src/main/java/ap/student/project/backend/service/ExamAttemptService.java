package ap.student.project.backend.service;

import ap.student.project.backend.dao.ExamAttemptRepository;
import ap.student.project.backend.dto.ExamAttemptDTO;
import ap.student.project.backend.entity.ExamAttempt;
import ap.student.project.backend.entity.UserTraining;
import ap.student.project.backend.exceptions.MissingArgumentException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for managing exam attempts.
 * Handles creating and retrieving exam attempt records.
 */
@Service
public class ExamAttemptService {
    private final ExamAttemptRepository examAttemptRepository;
    private final UserTrainingService userTrainingService;

    /**
     * Constructs a new ExamAttemptService with the required repositories and services.
     *
     * @param examAttemptRepository Repository for ExamAttempt entity operations
     * @param userTrainingService Service for user training-related operations
     */
    public ExamAttemptService(ExamAttemptRepository examAttemptRepository, UserTrainingService userTrainingService) {
        this.examAttemptRepository = examAttemptRepository;
        this.userTrainingService = userTrainingService;
    }

    /**
     * Creates and saves a new exam attempt.
     *
     * @param examAttemptDTO Data transfer object containing exam attempt information
     * @return The saved ExamAttempt entity
     * @throws MissingArgumentException If user_training_id is missing from the DTO
     * @throws NotFoundException If the user training is not found
     */
    public ExamAttempt save(ExamAttemptDTO examAttemptDTO) {
        ExamAttempt examAttempt = new ExamAttempt();
        if(examAttemptDTO.userTrainingId()==0) {
            throw new MissingArgumentException("user_training_id is missing");
        }
        UserTraining userTraining = userTrainingService.findById(examAttemptDTO.userTrainingId());
        examAttempt.setUserTraining(userTraining);
        BeanUtils.copyProperties(examAttemptDTO, examAttempt);
        return examAttemptRepository.save(examAttempt);
    }

    /**
     * Retrieves all exam attempts in the system.
     *
     * @return A list of all ExamAttempt entities
     */
    public List<ExamAttempt> findAll() {
        return examAttemptRepository.findAll();
    }

    /**
     * Counts the number of failed exam attempts for a specific user training.
     *
     * @param userTrainingId The ID of the user training
     * @return The count of failed attempts
     */
    public int countFailedAttemptsByUserTrainingId(int userTrainingId) {
        return examAttemptRepository.countFailedAttemptsByUserTrainingId(userTrainingId);
    }

    /**
     * Deletes all failed exam attempts for a specific user training.
     *
     * @param userTrainingId The ID of the user training
     */
    public void deleteFailedAttemptsByUserTrainingId(int userTrainingId) {
        List<ExamAttempt> failedAttempts = examAttemptRepository.findFailedAttemptsByUserTrainingId(userTrainingId);
        examAttemptRepository.deleteAll(failedAttempts);
    }
}
