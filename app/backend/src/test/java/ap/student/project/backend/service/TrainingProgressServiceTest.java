package ap.student.project.backend.service;

import ap.student.project.backend.dao.TrainingProgressRepository;
import ap.student.project.backend.dto.TrainingDTO;
import ap.student.project.backend.dto.TrainingProgressDTO;
import ap.student.project.backend.dto.UserDTO;
import ap.student.project.backend.dto.UserTrainingDTO;
import ap.student.project.backend.entity.*;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TrainingProgressServiceTest {

    @Autowired
    private TrainingProgressRepository trainingProgressRepository;

    @Autowired
    private UserTrainingService userTrainingService;

    @Autowired
    private TrainingProgressService trainingProgressService;

    @Autowired
    private UserService userService;

    @Autowired
    private TrainingService trainingService;

    @BeforeEach
    void setUp() {
        cleanDatabase();
    }

    @AfterEach
    void tearDown() {
        cleanDatabase();
    }

    private void cleanDatabase() {
        trainingProgressRepository.deleteAll();
    }

    @Test
    void save_ShouldThrowException_WhenUserTrainingIdIsZero() {
        TrainingProgressDTO invalidDTO = new TrainingProgressDTO(
                LocalDateTime.now(),
                LocalDateTime.now(),
                ProgressStatusType.IN_PROGRESS,
                0, 1
        );
        assertThrows(MissingArgumentException.class, () -> trainingProgressService.save(invalidDTO));
    }

    @Test
    @Transactional
    void save_ShouldSaveTrainingProgress_WhenValidDTOIsProvided() {
        // Create fresh test data for this test
        String uniqueUserId = "testId-" + UUID.randomUUID().toString();
        UserDTO userDTO = new UserDTO(uniqueUserId, "John", "Doe", "Ship123", Language.ENGLISH);
        User user = userService.save(userDTO);

        TrainingDTO trainingDTO = new TrainingDTO(
                Collections.singletonMap(Language.ENGLISH, "Test Training"),
                Collections.singletonMap(Language.ENGLISH, "Test Description"),
                true,
                null,
                null
        );
        Training training = trainingService.save(trainingDTO);

        UserTrainingDTO userTrainingDTO = new UserTrainingDTO(training.getId(), user.getId(), false);
        userTrainingService.save(userTrainingDTO);

        // Find the created UserTraining to use its ID
        UserTraining createdUserTraining = userTrainingService.findAll().stream()
                .filter(ut -> ut.getUser().getId() == user.getId() && ut.getTraining().getId() == training.getId())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("UserTraining not found"));

        TrainingProgressDTO trainingProgressDTO = new TrainingProgressDTO(
                LocalDateTime.now(),
                LocalDateTime.now(),
                ProgressStatusType.IN_PROGRESS,
                createdUserTraining.getId(), 1
        );

        // Make sure UserTraining does not already have a TrainingProgress
        assertNull(createdUserTraining.getTrainingProgress());

        TrainingProgress savedProgress = trainingProgressService.save(trainingProgressDTO);

        List<TrainingProgress> savedProgresses = trainingProgressRepository.findAll();
        assertFalse(savedProgresses.isEmpty());
        assertEquals(1, savedProgresses.size()); // Verify we have exactly one entry

        TrainingProgress retrievedProgress = savedProgresses.get(0);
        // Compare the actual returned object with the one in the database
        assertEquals(savedProgress.getId(), retrievedProgress.getId());
        assertEquals(createdUserTraining.getId(), retrievedProgress.getUserTraining().getId());
        assertEquals(trainingProgressDTO.status(), retrievedProgress.getStatus());
    }

    @Test
    @Transactional
    void update_ShouldSaveUpdatedTrainingProgress_WhenFound() {
        // Create fresh test data for this test
        String uniqueUserId = "testId-" + UUID.randomUUID().toString();
        UserDTO userDTO = new UserDTO(uniqueUserId, "John", "Doe", "Ship123", Language.ENGLISH);
        User user = userService.save(userDTO);

        TrainingDTO trainingDTO = new TrainingDTO(
                Collections.singletonMap(Language.ENGLISH, "Test Training"),
                Collections.singletonMap(Language.ENGLISH, "Test Description"),
                true,
                null,
                null
        );
        Training training = trainingService.save(trainingDTO);

        UserTrainingDTO userTrainingDTO = new UserTrainingDTO(training.getId(), user.getId(), false);
        userTrainingService.save(userTrainingDTO);

        // Find the created UserTraining to use its ID
        UserTraining createdUserTraining = userTrainingService.findAll().stream()
                .filter(ut -> ut.getUser().getId() == user.getId() && ut.getTraining().getId() == training.getId())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("UserTraining not found"));

        // Make sure UserTraining does not already have a TrainingProgress
        assertNull(createdUserTraining.getTrainingProgress());

        // Create and save initial TrainingProgress
        TrainingProgressDTO trainingProgressDTO = new TrainingProgressDTO(
                LocalDateTime.now(),
                LocalDateTime.now(),
                ProgressStatusType.IN_PROGRESS,
                createdUserTraining.getId(), 1
        );
        trainingProgressService.save(trainingProgressDTO);

        // Get the saved TrainingProgress
        TrainingProgress savedProgress = trainingProgressRepository.findAll().get(0);

        // Create updated DTO with new status
        TrainingProgressDTO updatedDTO = new TrainingProgressDTO(
                savedProgress.getStartDateTime(),
                LocalDateTime.now(),
                ProgressStatusType.COMPLETED,
                createdUserTraining.getId(), 1
        );

        trainingProgressService.update(savedProgress.getId(), updatedDTO);

        TrainingProgress updatedProgress = trainingProgressRepository.findById(savedProgress.getId()).orElseThrow();
        assertEquals(ProgressStatusType.COMPLETED, updatedProgress.getStatus());
    }

    @Test
    void update_ShouldThrowException_WhenTrainingProgressNotFound() {
        assertThrows(NotFoundException.class, () -> trainingProgressService.update(9999,
                new TrainingProgressDTO(
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        ProgressStatusType.COMPLETED,
                        1, 1
                )
        ));
    }

    @Test
    void findAll_ShouldReturnListOfTrainingProgress() {
        // Create fresh test data
        String uniqueUserId = "testId-" + UUID.randomUUID().toString();
        UserDTO userDTO = new UserDTO(uniqueUserId, "John", "Doe", "Ship123", Language.ENGLISH);
        User user = userService.save(userDTO);

        TrainingDTO trainingDTO = new TrainingDTO(
                Collections.singletonMap(Language.ENGLISH, "Test Training"),
                Collections.singletonMap(Language.ENGLISH, "Test Description"),
                true,
                null,
                null
        );
        Training training = trainingService.save(trainingDTO);

        UserTrainingDTO userTrainingDTO = new UserTrainingDTO(training.getId(), user.getId(), false);
        userTrainingService.save(userTrainingDTO);

        // Find the created UserTraining
        UserTraining createdUserTraining = userTrainingService.findAll().stream()
                .filter(ut -> ut.getUser().getId() == user.getId() && ut.getTraining().getId() == training.getId())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("UserTraining not found"));

        // Create and save TrainingProgress
        TrainingProgressDTO trainingProgressDTO = new TrainingProgressDTO(
                LocalDateTime.now(),
                LocalDateTime.now(),
                ProgressStatusType.IN_PROGRESS,
                createdUserTraining.getId(), 1
        );
        trainingProgressService.save(trainingProgressDTO);

        List<TrainingProgress> result = trainingProgressService.findAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void findById_ShouldReturnTrainingProgress_WhenExists() {
        // Create fresh test data
        String uniqueUserId = "testId-" + UUID.randomUUID().toString();
        UserDTO userDTO = new UserDTO(uniqueUserId, "John", "Doe", "Ship123", Language.ENGLISH);
        User user = userService.save(userDTO);

        TrainingDTO trainingDTO = new TrainingDTO(
                Collections.singletonMap(Language.ENGLISH, "Test Training"),
                Collections.singletonMap(Language.ENGLISH, "Test Description"),
                true,
                null,
                null
        );
        Training training = trainingService.save(trainingDTO);

        UserTrainingDTO userTrainingDTO = new UserTrainingDTO(training.getId(), user.getId(), false);
        userTrainingService.save(userTrainingDTO);

        UserTraining createdUserTraining = userTrainingService.findAll().stream()
                .filter(ut -> ut.getUser().getId() == user.getId() && ut.getTraining().getId() == training.getId())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("UserTraining not found"));

        // Create and save TrainingProgress
        TrainingProgressDTO trainingProgressDTO = new TrainingProgressDTO(
                LocalDateTime.now(),
                LocalDateTime.now(),
                ProgressStatusType.IN_PROGRESS,
                createdUserTraining.getId(),
                3
        );
        TrainingProgress savedProgress = trainingProgressService.save(trainingProgressDTO);

        // Test findById
        TrainingProgress foundProgress = trainingProgressService.findById(savedProgress.getId());

        assertNotNull(foundProgress);
        assertEquals(savedProgress.getId(), foundProgress.getId());
        assertEquals(ProgressStatusType.IN_PROGRESS, foundProgress.getStatus());
        assertEquals(3, foundProgress.getModulesCompleted());
        assertEquals(createdUserTraining.getId(), foundProgress.getUserTraining().getId());
    }

    @Test
    void findById_ShouldThrowException_WhenNotFound() {
        NotFoundException exception = assertThrows(NotFoundException.class, 
        () -> trainingProgressService.findById(9999));
        assertTrue(exception.getMessage().contains("Training progress with id 9999 not found"));
    }

    @Test
    void findByUserTrainingId_ShouldReturnTrainingProgress_WhenExists() {
        // Create fresh test data
        String uniqueUserId = "testId-" + UUID.randomUUID().toString();
        UserDTO userDTO = new UserDTO(uniqueUserId, "John", "Doe", "Ship123", Language.ENGLISH);
        User user = userService.save(userDTO);

        TrainingDTO trainingDTO = new TrainingDTO(
                Collections.singletonMap(Language.ENGLISH, "Test Training"),
                Collections.singletonMap(Language.ENGLISH, "Test Description"),
                true,
                null,
                null
        );
        Training training = trainingService.save(trainingDTO);

        UserTrainingDTO userTrainingDTO = new UserTrainingDTO(training.getId(), user.getId(), false);
        userTrainingService.save(userTrainingDTO);

        UserTraining createdUserTraining = userTrainingService.findAll().stream()
                .filter(ut -> ut.getUser().getId() == user.getId() && ut.getTraining().getId() == training.getId())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("UserTraining not found"));

        // Create and save TrainingProgress
        TrainingProgressDTO trainingProgressDTO = new TrainingProgressDTO(
                LocalDateTime.now(),
                LocalDateTime.now(),
                ProgressStatusType.COMPLETED,
                createdUserTraining.getId(),
                5
        );
        trainingProgressService.save(trainingProgressDTO);

        // Test findByUserTrainingId
        TrainingProgress foundProgress = trainingProgressService.findByUserTrainingId(createdUserTraining.getId());

        assertNotNull(foundProgress);
        assertEquals(ProgressStatusType.COMPLETED, foundProgress.getStatus());
        assertEquals(5, foundProgress.getModulesCompleted());
        assertEquals(createdUserTraining.getId(), foundProgress.getUserTraining().getId());
    }

    @Test
    void findByUserTrainingId_ShouldReturnNull_WhenNotExists() {
        TrainingProgress result = trainingProgressService.findByUserTrainingId(9999);
        assertNull(result);
    }

    @Test
    void save_ShouldThrowException_WhenUserTrainingAlreadyHasProgress() {
        // Create fresh test data
        String uniqueUserId = "testId-" + UUID.randomUUID().toString();
        UserDTO userDTO = new UserDTO(uniqueUserId, "John", "Doe", "Ship123", Language.ENGLISH);
        User user = userService.save(userDTO);

        TrainingDTO trainingDTO = new TrainingDTO(
                Collections.singletonMap(Language.ENGLISH, "Test Training"),
                Collections.singletonMap(Language.ENGLISH, "Test Description"),
                true,
                null,
                null
        );
        Training training = trainingService.save(trainingDTO);

        UserTrainingDTO userTrainingDTO = new UserTrainingDTO(training.getId(), user.getId(), false);
        userTrainingService.save(userTrainingDTO);

        // Find the created UserTraining
        UserTraining createdUserTraining = userTrainingService.findAll().stream()
                .filter(ut -> ut.getUser().getId() == user.getId() && ut.getTraining().getId() == training.getId())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("UserTraining not found"));

        // Create and save first TrainingProgress
        TrainingProgressDTO firstProgressDTO = new TrainingProgressDTO(
                LocalDateTime.now(),
                LocalDateTime.now(),
                ProgressStatusType.IN_PROGRESS,
                createdUserTraining.getId(),
                1
        );
        trainingProgressService.save(firstProgressDTO);

        // Try to create a second TrainingProgress for the same UserTraining
        TrainingProgressDTO secondProgressDTO = new TrainingProgressDTO(
                LocalDateTime.now(),
                LocalDateTime.now(),
                ProgressStatusType.NOT_STARTED,
                createdUserTraining.getId(),
                0
        );

        IllegalStateException exception = assertThrows(IllegalStateException.class, 
                () -> trainingProgressService.save(secondProgressDTO));
        assertTrue(exception.getMessage().contains("already has a TrainingProgress"));
    }

    @Test
    void addModuleCompleted_ShouldIncrementModulesCompleted_WhenTrainingProgressExists() {
        // Create fresh test data
        String uniqueUserId = "testId-" + UUID.randomUUID().toString();
        UserDTO userDTO = new UserDTO(uniqueUserId, "John", "Doe", "Ship123", Language.ENGLISH);
        User user = userService.save(userDTO);

        TrainingDTO trainingDTO = new TrainingDTO(
                Collections.singletonMap(Language.ENGLISH, "Test Training"),
                Collections.singletonMap(Language.ENGLISH, "Test Description"),
                true,
                null,
                null
        );
        Training training = trainingService.save(trainingDTO);

        UserTrainingDTO userTrainingDTO = new UserTrainingDTO(training.getId(), user.getId(), false);
        userTrainingService.save(userTrainingDTO);

        UserTraining createdUserTraining = userTrainingService.findAll().stream()
                .filter(ut -> ut.getUser().getId() == user.getId() && ut.getTraining().getId() == training.getId())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("UserTraining not found"));

        // Create and save TrainingProgress with initial modules completed
        TrainingProgressDTO trainingProgressDTO = new TrainingProgressDTO(
                LocalDateTime.now(),
                LocalDateTime.now(),
                ProgressStatusType.IN_PROGRESS,
                createdUserTraining.getId(),
                2
        );
        TrainingProgress savedProgress = trainingProgressService.save(trainingProgressDTO);

        // Test addModuleCompleted
        TrainingProgress updatedProgress = trainingProgressService.addModuleCompleted(savedProgress.getId());

        assertEquals(3, updatedProgress.getModulesCompleted());
        
        // Verify the change was persisted
        TrainingProgress persistedProgress = trainingProgressService.findById(savedProgress.getId());
        assertEquals(3, persistedProgress.getModulesCompleted());
    }

    @Test
    void addModuleCompleted_ShouldThrowException_WhenTrainingProgressNotFound() {
        NotFoundException exception = assertThrows(NotFoundException.class, 
                () -> trainingProgressService.addModuleCompleted(9999));
        assertTrue(exception.getMessage().contains("Training progress with id"));
    }

    @Test
    @Transactional
    void resetTrainingProgress_ShouldDeleteProgressAndBreakRelationship_WhenProgressExists() {
        // Create fresh test data
        String uniqueUserId = "testId-" + UUID.randomUUID().toString();
        UserDTO userDTO = new UserDTO(uniqueUserId, "John", "Doe", "Ship123", Language.ENGLISH);
        User user = userService.save(userDTO);

        TrainingDTO trainingDTO = new TrainingDTO(
                Collections.singletonMap(Language.ENGLISH, "Test Training"),
                Collections.singletonMap(Language.ENGLISH, "Test Description"),
                true,
                null,
                null
        );
        Training training = trainingService.save(trainingDTO);

        UserTrainingDTO userTrainingDTO = new UserTrainingDTO(training.getId(), user.getId(), false);
        userTrainingService.save(userTrainingDTO);

        UserTraining createdUserTraining = userTrainingService.findAll().stream()
                .filter(ut -> ut.getUser().getId() == user.getId() && ut.getTraining().getId() == training.getId())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("UserTraining not found"));

        // Create and save TrainingProgress
        TrainingProgressDTO trainingProgressDTO = new TrainingProgressDTO(
                LocalDateTime.now(),
                LocalDateTime.now(),
                ProgressStatusType.IN_PROGRESS,
                createdUserTraining.getId(),
                3
        );
        TrainingProgress savedProgress = trainingProgressService.save(trainingProgressDTO);

        // Verify progress exists and relationship is established
        assertNotNull(trainingProgressService.findByUserTrainingId(createdUserTraining.getId()));
        UserTraining userTrainingBeforeReset = userTrainingService.findById(createdUserTraining.getId());
        assertNotNull(userTrainingBeforeReset.getTrainingProgress());

        // Reset training progress
        trainingProgressService.resetTrainingProgress(createdUserTraining.getId());

        // Verify progress is deleted
        assertNull(trainingProgressService.findByUserTrainingId(createdUserTraining.getId()));
        
        // Verify relationship is broken
        UserTraining userTrainingAfterReset = userTrainingService.findById(createdUserTraining.getId());
        assertNull(userTrainingAfterReset.getTrainingProgress());

        // Verify progress no longer exists in database
        assertThrows(NotFoundException.class, () -> trainingProgressService.findById(savedProgress.getId()));
    }

    @Test
    @Transactional
    void resetTrainingProgress_ShouldDoNothing_WhenNoProgressExists() {
        // Create fresh test data without training progress
        String uniqueUserId = "testId-" + UUID.randomUUID().toString();
        UserDTO userDTO = new UserDTO(uniqueUserId, "John", "Doe", "Ship123", Language.ENGLISH);
        User user = userService.save(userDTO);

        TrainingDTO trainingDTO = new TrainingDTO(
                Collections.singletonMap(Language.ENGLISH, "Test Training"),
                Collections.singletonMap(Language.ENGLISH, "Test Description"),
                true,
                null,
                null
        );
        Training training = trainingService.save(trainingDTO);

        UserTrainingDTO userTrainingDTO = new UserTrainingDTO(training.getId(), user.getId(), false);
        userTrainingService.save(userTrainingDTO);

        UserTraining createdUserTraining = userTrainingService.findAll().stream()
                .filter(ut -> ut.getUser().getId() == user.getId() && ut.getTraining().getId() == training.getId())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("UserTraining not found"));

        // Verify no progress exists initially
        assertNull(trainingProgressService.findByUserTrainingId(createdUserTraining.getId()));

        // This should not throw an exception and should do nothing
        assertDoesNotThrow(() -> trainingProgressService.resetTrainingProgress(createdUserTraining.getId()));

        // Verify still no progress exists
        assertNull(trainingProgressService.findByUserTrainingId(createdUserTraining.getId()));
    }

    @Test
    void resetTrainingProgress_ShouldDoNothing_WhenUserTrainingNotFound() {
        // This should not throw an exception for non-existent userTrainingId
        assertDoesNotThrow(() -> trainingProgressService.resetTrainingProgress(9999));
    }

    @Test
    @Transactional
    void save_ShouldEstablishBidirectionalRelationship() {
        // Create fresh test data
        String uniqueUserId = "testId-" + UUID.randomUUID().toString();
        UserDTO userDTO = new UserDTO(uniqueUserId, "John", "Doe", "Ship123", Language.ENGLISH);
        User user = userService.save(userDTO);

        TrainingDTO trainingDTO = new TrainingDTO(
                Collections.singletonMap(Language.ENGLISH, "Test Training"),
                Collections.singletonMap(Language.ENGLISH, "Test Description"),
                true,
                null,
                null
        );
        Training training = trainingService.save(trainingDTO);

        UserTrainingDTO userTrainingDTO = new UserTrainingDTO(training.getId(), user.getId(), false);
        userTrainingService.save(userTrainingDTO);

        UserTraining createdUserTraining = userTrainingService.findAll().stream()
                .filter(ut -> ut.getUser().getId() == user.getId() && ut.getTraining().getId() == training.getId())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("UserTraining not found"));

        // Initially, UserTraining should not have TrainingProgress
        assertNull(createdUserTraining.getTrainingProgress());

        TrainingProgressDTO trainingProgressDTO = new TrainingProgressDTO(
                LocalDateTime.now(),
                LocalDateTime.now(),
                ProgressStatusType.NOT_STARTED,
                createdUserTraining.getId(),
                0
        );

        TrainingProgress savedProgress = trainingProgressService.save(trainingProgressDTO);

        // Verify bidirectional relationship
        // TrainingProgress should point to UserTraining
        assertEquals(createdUserTraining.getId(), savedProgress.getUserTraining().getId());
        
        // UserTraining should now point to TrainingProgress
        UserTraining updatedUserTraining = userTrainingService.findById(createdUserTraining.getId());
        assertNotNull(updatedUserTraining.getTrainingProgress());
        assertEquals(savedProgress.getId(), updatedUserTraining.getTrainingProgress().getId());
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenNoTrainingProgressExists() {
        List<TrainingProgress> result = trainingProgressService.findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    @Transactional
    void update_ShouldOnlyUpdateNonRelationshipFields() {
        // Create fresh test data
        String uniqueUserId = "testId-" + UUID.randomUUID().toString();
        UserDTO userDTO = new UserDTO(uniqueUserId, "John", "Doe", "Ship123", Language.ENGLISH);
        User user = userService.save(userDTO);

        TrainingDTO trainingDTO = new TrainingDTO(
                Collections.singletonMap(Language.ENGLISH, "Test Training"),
                Collections.singletonMap(Language.ENGLISH, "Test Description"),
                true,
                null,
                null
        );
        Training training = trainingService.save(trainingDTO);

        UserTrainingDTO userTrainingDTO = new UserTrainingDTO(training.getId(), user.getId(), false);
        userTrainingService.save(userTrainingDTO);

        UserTraining createdUserTraining = userTrainingService.findAll().stream()
                .filter(ut -> ut.getUser().getId() == user.getId() && ut.getTraining().getId() == training.getId())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("UserTraining not found"));

        // Create and save initial TrainingProgress
        LocalDateTime originalStartTime = LocalDateTime.now().minusDays(1);
        LocalDateTime originalAccessTime = LocalDateTime.now().minusDays(1);
        
        TrainingProgressDTO trainingProgressDTO = new TrainingProgressDTO(
                originalStartTime,
                originalAccessTime,
                ProgressStatusType.IN_PROGRESS,
                createdUserTraining.getId(),
                2
        );
        TrainingProgress savedProgress = trainingProgressService.save(trainingProgressDTO);

        // Create updated DTO with different userTrainingId (should be ignored)
        LocalDateTime newStartTime = LocalDateTime.now();
        LocalDateTime newAccessTime = LocalDateTime.now();
        
        TrainingProgressDTO updatedDTO = new TrainingProgressDTO(
                newStartTime,
                newAccessTime,
                ProgressStatusType.COMPLETED,
                99999, // Different userTrainingId - should be ignored
                5
        );

        TrainingProgress updatedProgress = trainingProgressService.update(savedProgress.getId(), updatedDTO);

        // Non-relationship fields should be updated
        assertEquals(ProgressStatusType.COMPLETED, updatedProgress.getStatus());
        assertEquals(5, updatedProgress.getModulesCompleted());
        assertEquals(newStartTime, updatedProgress.getStartDateTime());
        assertEquals(newAccessTime, updatedProgress.getLastTimeAccessed());
        
        // Relationship should remain unchanged
        assertEquals(createdUserTraining.getId(), updatedProgress.getUserTraining().getId());
    }

}