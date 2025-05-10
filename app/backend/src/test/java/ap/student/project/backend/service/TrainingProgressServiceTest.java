package ap.student.project.backend.service;

import ap.student.project.backend.dao.TrainingProgressRepository;
import ap.student.project.backend.dto.TrainingDTO;
import ap.student.project.backend.dto.TrainingProgressDTO;
import ap.student.project.backend.dto.UserDTO;
import ap.student.project.backend.dto.UserTrainingDTO;
import ap.student.project.backend.entity.Language;
import ap.student.project.backend.entity.ProgressStatusType;
import ap.student.project.backend.entity.Training;
import ap.student.project.backend.entity.TrainingProgress;
import ap.student.project.backend.entity.User;
import ap.student.project.backend.entity.UserTraining;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
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
        // Clean up database
        trainingProgressRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        // Clean up database
        trainingProgressRepository.deleteAll();
    }

    @Test
    void save_ShouldThrowException_WhenUserTrainingIdIsZero() {
        TrainingProgressDTO invalidDTO = new TrainingProgressDTO(
            LocalDateTime.now(), 
            LocalDateTime.now(), 
            ProgressStatusType.IN_PROGRESS, 
            null, 
            0
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
        
        UserTrainingDTO userTrainingDTO = new UserTrainingDTO(null, training.getId(), user.getId());
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
            null, 
            createdUserTraining.getId()
        );
        
        // Make sure UserTraining does not already have a TrainingProgress
        assertNull(createdUserTraining.getTrainingProgress());
        
        trainingProgressService.save(trainingProgressDTO);
        
        List<TrainingProgress> savedProgresses = trainingProgressRepository.findAll();
        assertFalse(savedProgresses.isEmpty());
        TrainingProgress savedProgress = savedProgresses.get(0);
        assertEquals(createdUserTraining.getId(), savedProgress.getUserTraining().getId());
        assertEquals(trainingProgressDTO.status(), savedProgress.getStatus());
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
        
        UserTrainingDTO userTrainingDTO = new UserTrainingDTO(null, training.getId(), user.getId());
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
            null, 
            createdUserTraining.getId()
        );
        trainingProgressService.save(trainingProgressDTO);
        
        // Get the saved TrainingProgress
        TrainingProgress savedProgress = trainingProgressRepository.findAll().get(0);
        
        // Create updated DTO with new status
        TrainingProgressDTO updatedDTO = new TrainingProgressDTO(
            savedProgress.getStartDateTime(),
            LocalDateTime.now(),
            ProgressStatusType.COMPLETED,
            null,
            createdUserTraining.getId()
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
                null,
                1
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
        
        UserTrainingDTO userTrainingDTO = new UserTrainingDTO(null, training.getId(), user.getId());
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
            null, 
            createdUserTraining.getId()
        );
        trainingProgressService.save(trainingProgressDTO);
        
        List<TrainingProgress> result = trainingProgressService.findAll();
        
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}