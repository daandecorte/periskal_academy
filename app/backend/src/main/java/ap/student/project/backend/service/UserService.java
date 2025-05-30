package ap.student.project.backend.service;

import ap.student.project.backend.dao.UserRepository;
import ap.student.project.backend.dto.UserDTO;
import ap.student.project.backend.entity.*;
import ap.student.project.backend.exceptions.DuplicateException;
import ap.student.project.backend.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class that manages operations related to User entities.
 * This service handles the business logic for creating, retrieving, updating user records,
 * and manages relationships between users and their associated entities.
 */
@Service
public class UserService {
    private final UserRepository userRepository;

    /**
     * Constructs a new UserService with the necessary dependencies.
     *
     * @param userRepository The repository used for User entity persistence operations
     */
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creates and saves a new User from the provided DTO.
     * This method verifies that no user with the same periskalId already exists.
     *
     * @param userDTO The data transfer object containing the User information
     * @return The newly created and saved User entity
     * @throws DuplicateException If a user with the same periskalId already exists
     */
    public User save(UserDTO userDTO) throws DuplicateException {
        User user = assemble(userDTO);
        if (userRepository.existsByPeriskalId(user.getPeriskalId())) {
            throw new DuplicateException("User with userid " + user.getPeriskalId() + " already exists");
        }
        return userRepository.save(user);
    }

    /**
     * Updates an existing User identified by periskalId with new information from the provided DTO.
     *
     * @param userDTO The data transfer object containing the updated User information
     * @throws NotFoundException If no User with the given periskalId exists
     */
    public void update(UserDTO userDTO) throws NotFoundException {
        User updatedUser = userRepository.findByPeriskalId(userDTO.periskalId());
        if (updatedUser == null) {
            throw new NotFoundException("User with userid " + userDTO.periskalId() + " not found");
        }
        updatedUser.setLanguage(userDTO.language());
        userRepository.save(updatedUser);
    }

    /**
     * Finds a User by ID.
     *
     * @param id The ID of the User to find
     * @return The found User entity
     * @throws NotFoundException If no User with the given ID exists
     */
    public User findById(int id) throws NotFoundException {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new NotFoundException("User with id " + id + " not found");
        }
        return user;
    }

    /**
     * Finds a User by periskalId.
     *
     * @param periskalId The periskalId of the User to find
     * @return The found User entity
     * @throws NotFoundException If no User with the given periskalId exists
     */
    public User findByPeriskalId(String periskalId) throws NotFoundException {
        User user = userRepository.findByPeriskalId(periskalId);
        if (user == null) {
            throw new NotFoundException("User with periskal id " + periskalId + " not found");
        }
        return user;
    }

    /**
     * Checks if a User with the specified periskalId exists.
     *
     * @param userId The periskalId to check
     * @return true if a User with the given periskalId exists, false otherwise
     */
    public boolean existsByPeriskalId(String userId) {
        User user = userRepository.findByPeriskalId(userId);
        return user != null;
    }

    /**
     * Retrieves all Users from the database.
     *
     * @return A list containing all User entities
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Retrieves all UserTrainings associated with a specific user ID.
     *
     * @param id The ID of the User whose training modules to find
     * @return A list of UserTraining entities associated with the specified user
     * @throws NotFoundException If the user with the given ID does not exist or has no training modules
     */
    public List<UserTraining> getAllUserModules(int id) throws NotFoundException {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) throw new NotFoundException("user not found");
        List<UserTraining> userTrainings = user.getUserTrainings();
        if (userTrainings == null) throw new NotFoundException("user does not have any user trainings");
        return userTrainings;
    }

    /**
     * Retrieves all UserCertificates associated with a specific user ID.
     *
     * @param id The ID of the User whose certificates to find
     * @return A list of UserCertificates entities associated with the specified user
     * @throws NotFoundException If the user with the given ID does not exist or has no training modules
     */
    public List<UserCertificate> getAllUserCertificates(int id) throws NotFoundException {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) throw new NotFoundException("user not found");
        List<UserCertificate> userCertificates = user.getUserCertificates();
        if (userCertificates == null) throw new NotFoundException("user does not have any user certificates");
        return userCertificates;
    }

    /**
     * Retrieves all ExamAttempts associated with a specific user ID.
     * This method collects all exam attempts across all of the user's training modules.
     *
     * @param id The ID of the User whose exam attempts to find
     * @return A list of ExamAttempt entities associated with the specified user
     * @throws NotFoundException If the user with the given ID does not exist, has no training modules, or has no exam attempts
     */
    public List<ExamAttempt> getAllExamAttempts(int id) throws NotFoundException {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) throw new NotFoundException("user not found");
        List<UserTraining> userTrainings = user.getUserTrainings();
        if (userTrainings == null) throw new NotFoundException("user does not have any user trainings");
        List<ExamAttempt> examAttempts = new ArrayList<>();
        for (UserTraining userTraining : userTrainings) {
            examAttempts.addAll(userTraining.getExamAttempts());
        }
        if (examAttempts.isEmpty()) throw new NotFoundException("user does not have any exam attempts");
        return examAttempts;
    }

    /**
     * Retrieves all ChatMember entities associated with a specific user ID.
     *
     * @param id The ID of the User whose chat memberships to find
     * @return A list of ChatMember entities associated with the specified user
     * @throws NotFoundException If the user with the given ID does not exist or has no chat memberships
     */
    public List<ChatMember> getChatMember(int id) throws NotFoundException {
        User user = this.findById(id);
        List<ChatMember> chatMembers = user.getChatMembers();
        if (chatMembers.isEmpty()) {
            throw new NotFoundException("this user has no chatmembers");
        }
        return chatMembers;
    }

    /**
     * Creates a new User entity from the provided DTO.
     * This is a helper method used internally by other service methods.
     *
     * @param userDTO The data transfer object containing the User information
     * @return A new User entity populated with data from the DTO
     */
    public User assemble(UserDTO userDTO) {
        return new User(userDTO.periskalId(), userDTO.firstname(), userDTO.lastname(), userDTO.shipname(), userDTO.language());
    }
}
