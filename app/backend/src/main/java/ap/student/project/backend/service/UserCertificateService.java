package ap.student.project.backend.service;

import ap.student.project.backend.dao.UserCertificateRepository;
import ap.student.project.backend.dto.UserCertificateDTO;
import ap.student.project.backend.entity.Certificate;
import ap.student.project.backend.entity.Training;
import ap.student.project.backend.entity.User;
import ap.student.project.backend.entity.UserCertificate;
import ap.student.project.backend.exceptions.DuplicateException;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class that manages operations related to UserCertificate entities.
 * This service handles the business logic for creating, retrieving, and managing
 * certificate records associated with users.
 */
@Service
public class UserCertificateService {
    private final UserCertificateRepository userCertificateRepository;
    private final UserService userService;
    private final CertificateService certificateService;
    private final TrainingService trainingService;

    /**
     * Constructs a new UserCertificateService with the necessary dependencies.
     *
     * @param userCertificateRepository The repository used for UserCertificate entity persistence operations
     * @param userService               The service used for User operations
     * @param certificateService        The service used for Certificate operations
     */
    public UserCertificateService(UserCertificateRepository userCertificateRepository, UserService userService, CertificateService certificateService, TrainingService trainingService) {
        this.userCertificateRepository = userCertificateRepository;
        this.userService = userService;
        this.certificateService = certificateService;
        this.trainingService = trainingService;
    }

    /**
     * Creates and saves a new UserCertificate entity from the provided DTO.
     * This method establishes relationships between the UserCertificate and its associated User and Certificate.
     *
     * @param userCertificateDTO The data transfer object containing the UserCertificate information
     * @return The newly created and saved UserCertificate entity
     * @throws MissingArgumentException If either user_id or certificate_id is missing from the DTO
     * @throws DuplicateException       If the combination of user id and certificate id already exists in a usercertificate.
     */

    public UserCertificate save(UserCertificateDTO userCertificateDTO) {
        UserCertificate userCertificate = new UserCertificate();
        if (userCertificateDTO.user_id() == 0) {
            throw new MissingArgumentException("user_id is missing");
        }
        if (userCertificateDTO.certificate_id() == 0) {
            throw new MissingArgumentException("certificate_id is missing");
        }
        //very important
        if (this.userCertificateRepository.findByUserIdAndCertificateId(userCertificateDTO.user_id(), userCertificateDTO.certificate_id()).isPresent()) {
            throw new DuplicateException("user certificate already exists");
        }
        userCertificate.setUser(userService.findById(userCertificateDTO.user_id()));
        userCertificate.setCertificate(certificateService.findById(userCertificateDTO.certificate_id()));
        userCertificate.setStatus(userCertificateDTO.status());
        userCertificate.setIssueDate(userCertificateDTO.issueDate());
        userCertificate.setExpiryDate(userCertificateDTO.expiryDate());
        return userCertificateRepository.save(userCertificate);
    }

    /**
     * Retrieves all UserCertificate entities from the database.
     *
     * @return A list containing all UserCertificate entities
     */
    public List<UserCertificate> findAll() {
        return userCertificateRepository.findAll();
    }

    /**
     * Finds all UserCertificates associated with a specific user ID.
     *
     * @param id The ID of the User whose certificates to find
     * @return A list of UserCertificate entities associated with the specified user
     * @throws NotFoundException If the user with the given ID has no certificates
     */
    public List<UserCertificate> findByUserId(int id) {
        User user = this.userService.findById(id);
        if (user.getUserCertificates().isEmpty()) {
            throw new NotFoundException("User with id " + id + " has no certificates");
        }
        return user.getUserCertificates();
    }

    /**
     * Finds a UserCertificate by its ID.
     *
     * @param id The ID of the UserCertificate to find
     * @return The found UserCertificate entity
     * @throws NotFoundException If no UserCertificate with the given ID exists
     */
    public UserCertificate findById(int id) {
        UserCertificate userCertificate = this.userCertificateRepository.findById(id).orElse(null);
        if (userCertificate == null) {
            throw new NotFoundException("User Certificate with id " + id + " not found");
        }
        return userCertificate;
    }

    /**
     * Finds a UserCertifiate by a TrainingId and a UserId
     *
     * @param trainingId
     * @param userId
     * @return The found user certificate
     */
    public UserCertificate findByTrainingIdAndUserId(int trainingId, int userId) {
        if (trainingId == 0) {
            throw new MissingArgumentException("training_id is missing");
        }
        if (userId == 0) {
            throw new MissingArgumentException("user_id is missing");
        }
        Training training = trainingService.findById(trainingId);
        Certificate certificate = training.getCertificate();
        UserCertificate userCertificate = this.userCertificateRepository.findByUserIdAndCertificateId(userId, certificate.getId()).orElse(null);
        if (userCertificate == null) {
            throw new NotFoundException("User Certificate With CertificateId " + certificate.getId() + " And UserId " + userId + " Not Found");
        }
        return userCertificate;
    }
}
