package ap.student.project.backend.service;

import ap.student.project.backend.dao.UserTrainingRepository;
import ap.student.project.backend.dto.UserTrainingDTO;
import ap.student.project.backend.entity.Training;
import ap.student.project.backend.entity.User;
import ap.student.project.backend.entity.UserTraining;
import ap.student.project.backend.exceptions.DuplicateException;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class that manages operations related to UserTraining entities.
 * This service handles the business logic for creating, retrieving UserTraining records,
 * which represent the association between a User and a Training.
 */
@Service
public class UserTrainingService {

    private final UserTrainingRepository userTrainingRepository;
    private final UserService userService;
    private final TrainingService trainingService;


    /**
     * Constructs a new UserTrainingService with the necessary dependencies.
     *
     * @param userTrainingRepository The repository used for UserTraining entity persistence operations
     * @param userService            The service used for User operations
     * @param trainingService        The service used for Training operations
     */
    @Autowired
    public UserTrainingService(UserTrainingRepository userTrainingRepository, UserService userService, TrainingService trainingService) {
        this.userTrainingRepository = userTrainingRepository;
        this.userService = userService;
        this.trainingService = trainingService;
    }

    /**
     * Creates and saves a new UserTraining from the provided DTO.
     * This method establishes the relationship between a User and a Training.
     *
     * @param userTrainingDTO The data transfer object containing the UserTraining information
     * @throws MissingArgumentException If either user_id or training_id is missing from the DTO
     * @throws DuplicateException       If the combination of training and user already exists within this table
     */
    public void save(UserTrainingDTO userTrainingDTO) {
        UserTraining userTraining = new UserTraining();
        if (userTrainingDTO.user_id() == 0) {
            throw new MissingArgumentException("user_id is missing");
        }
        if (userTrainingDTO.training_id() == 0) {
            throw new MissingArgumentException("training_id is missing");
        }
        if (this.userTrainingRepository.findByTrainingIdAndUserId(userTrainingDTO.training_id(), userTrainingDTO.user_id()).isPresent()) {
            throw new DuplicateException("user training with user_id " + userTrainingDTO.user_id() + " and training id " + userTrainingDTO.training_id() + " already exists");
        }
        User user = userService.findById(userTrainingDTO.user_id());
        Training training = trainingService.findById(userTrainingDTO.training_id());
        userTraining.setTraining(training);
        userTraining.setUser(user);
        userTraining.setEligibleForCertificate(userTrainingDTO.eligibleForCertificate());
        userTrainingRepository.save(userTraining);
    }

    /**
     * Finds a UserTraining by its ID.
     *
     * @param id The ID of the UserTraining to find
     * @return The found UserTraining entity
     * @throws NotFoundException If no UserTraining with the given ID exists
     */
    public UserTraining findById(int id) throws NotFoundException {
        UserTraining userTraining = this.userTrainingRepository.findById(id).orElse(null);
        if (userTraining == null) {
            throw new NotFoundException("User Training With Id " + id + " Not Found");
        }
        return userTraining;
    }

    /**
     * Finds a UserTraining by a user ID and a training ID.
     *
     * @param trainingId The ID of the training
     * @param userId     The ID of the user
     * @return The found UserTraining entity
     * @throws NotFoundException If no UserTraining with the given ID exists
     */
    public UserTraining findByTrainingIdAndUserId(int trainingId, int userId) throws NotFoundException {
        UserTraining userTraining = this.userTrainingRepository.findByTrainingIdAndUserId(trainingId, userId).orElse(null);
        if (userTraining == null) {
            throw new NotFoundException("User Training With TrainingId " + trainingId + " And UserId " + userId + " Not Found");
        }
        return userTraining;
    }

    /**
     * Updates a UserTraining object.
     *
     * @param id              The ID of the userTraining
     * @param userTrainingDTO The data transfer object containing the UserTraining information
     */
    public void update(int id, UserTrainingDTO userTrainingDTO) {
        UserTraining userTraining = this.findById(id);
        userTraining.setEligibleForCertificate(userTrainingDTO.eligibleForCertificate());
        userTrainingRepository.save(userTraining);
    }

    /**
     * Retrieves all UserTrainings from the database.
     *
     * @return A list containing all UserTraining entities
     */
    public List<UserTraining> findAll() {
        return userTrainingRepository.findAll();
    }
}
