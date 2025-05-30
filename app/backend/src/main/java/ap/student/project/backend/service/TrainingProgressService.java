package ap.student.project.backend.service;

import ap.student.project.backend.dao.TrainingProgressRepository;
import ap.student.project.backend.dto.TrainingProgressDTO;
import ap.student.project.backend.entity.TrainingProgress;
import ap.student.project.backend.entity.UserTraining;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class that manages operations related to TrainingProgress entities.
 * This service handles the business logic for creating, retrieving, and updating
 * training progress records associated with user trainings.
 */
@Service
public class TrainingProgressService {
    private final TrainingProgressRepository trainingProgressRepository;
    private final UserTrainingService userTrainingService;

    /**
     * Constructs a new TrainingProgressService with the necessary dependencies.
     *
     * @param trainingProgressRepository The repository used for TrainingProgress entity persistence operations
     * @param userTrainingService        The service used for UserTraining operations
     */
    public TrainingProgressService(TrainingProgressRepository trainingProgressRepository, UserTrainingService userTrainingService) {
        this.trainingProgressRepository = trainingProgressRepository;
        this.userTrainingService = userTrainingService;
    }

    /**
     * Creates and saves a new TrainingProgress entity from the provided DTO.
     * This method establishes a bidirectional relationship between the TrainingProgress
     * and its associated UserTraining.
     *
     * @param trainingProgressDTO The data transfer object containing the TrainingProgress information
     * @return The newly created and saved TrainingProgress entity
     * @throws MissingArgumentException If the userTrainingId is missing from the DTO
     * @throws IllegalStateException    If the specified UserTraining already has an associated TrainingProgress
     */
    public TrainingProgress save(TrainingProgressDTO trainingProgressDTO) {
        if (trainingProgressDTO.userTrainingId() == 0) {
            throw new MissingArgumentException("user_training_id is missing");
        }
        UserTraining userTraining = userTrainingService.findById(trainingProgressDTO.userTrainingId());

        // Check if this UserTraining already has a TrainingProgress
        if (userTraining.getTrainingProgress() != null) {
            throw new IllegalStateException("UserTraining with id " + userTraining.getId() +
                    " already has a TrainingProgress with id " + userTraining.getTrainingProgress().getId());
        }

        TrainingProgress trainingProgress = new TrainingProgress();
        trainingProgress.setUserTraining(userTraining);
        trainingProgress.setStartDateTime(trainingProgressDTO.startDateTime());
        trainingProgress.setLastTimeAccessed(trainingProgressDTO.lastTimeAccessed());
        trainingProgress.setStatus(trainingProgressDTO.status());
        trainingProgress.setModulesCompleted(trainingProgressDTO.modulesCompleted());

        TrainingProgress savedProgress = trainingProgressRepository.save(trainingProgress);

        // Establish bidirectional relationship
        userTraining.setTrainingProgress(savedProgress);

        return savedProgress;
    }

    /**
     * Updates an existing TrainingProgress entity with new information from the provided DTO.
     * Only non-relationship fields are updated.
     *
     * @param id                  The ID of the TrainingProgress to update
     * @param trainingProgressDTO The data transfer object containing the updated TrainingProgress information
     * @return The updated TrainingProgress entity
     * @throws NotFoundException If no TrainingProgress with the given ID exists
     */
    public TrainingProgress update(int id, TrainingProgressDTO trainingProgressDTO) throws NotFoundException {
        TrainingProgress trainingProgress = trainingProgressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Training progress with id " + id + " not found"));

        // Only update fields that don't involve relationships
        trainingProgress.setStartDateTime(trainingProgressDTO.startDateTime());
        trainingProgress.setLastTimeAccessed(trainingProgressDTO.lastTimeAccessed());
        trainingProgress.setStatus(trainingProgressDTO.status());
        trainingProgress.setModulesCompleted(trainingProgressDTO.modulesCompleted());
        return trainingProgressRepository.save(trainingProgress);
    }

    /**
     * Retrieves all TrainingProgress entities from the database.
     *
     * @return A list containing all TrainingProgress entities
     */
    public List<TrainingProgress> findAll() {
        return trainingProgressRepository.findAll();
    }

    /**
     * Finds a TrainingProgress entity by its ID.
     *
     * @param id The ID of the TrainingProgress to find
     * @return The found TrainingProgress entity
     * @throws NotFoundException If no TrainingProgress with the given ID exists
     */
    public TrainingProgress findById(int id) throws NotFoundException {
        return trainingProgressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Training progress with id " + id + " not found"));
    }

    /**
     * Finds a TrainingProgress entity by its associated UserTraining ID.
     *
     * @param userTrainingId The ID of the UserTraining associated with the TrainingProgress
     * @return The found TrainingProgress entity
     */
    public TrainingProgress findByUserTrainingId(int userTrainingId) {
        return trainingProgressRepository.findByUserTrainingId(userTrainingId);
    }

    public TrainingProgress addModuleCompleted(int trainingProgressId) {
        TrainingProgress trainingProgress = trainingProgressRepository.findById(trainingProgressId).orElseThrow(() -> new NotFoundException("Training progress with id \" + id + \" not found"));
        trainingProgress.setModulesCompleted(trainingProgress.getModulesCompleted() + 1);
        return trainingProgressRepository.save(trainingProgress);
    }

    /**
     * Resets the training progress for a user by deleting their existing progress.
     *
     * @param userTrainingId The ID of the user training whose progress should be reset
     */
    @Transactional
    public void resetTrainingProgress(int userTrainingId) {
        TrainingProgress trainingProgress = trainingProgressRepository.findByUserTrainingId(userTrainingId);
        if (trainingProgress != null) {
            // Get the UserTraining to break the bidirectional relationship
            UserTraining userTraining = trainingProgress.getUserTraining();
            if (userTraining != null) {
                userTraining.setTrainingProgress(null);
            }

            // Delete the training progress
            trainingProgressRepository.delete(trainingProgress);
        }
    }
}
