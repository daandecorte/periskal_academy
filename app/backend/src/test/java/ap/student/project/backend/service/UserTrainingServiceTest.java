package ap.student.project.backend.service;

import ap.student.project.backend.dao.UserTrainingRepository;
import ap.student.project.backend.dto.TrainingDTO;
import ap.student.project.backend.dto.UserDTO;
import ap.student.project.backend.dto.UserTrainingDTO;
import ap.student.project.backend.entity.Language;
import ap.student.project.backend.entity.Training;
import ap.student.project.backend.entity.User;
import ap.student.project.backend.entity.UserTraining;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class UserTrainingServiceTest {

    @Autowired
    private UserTrainingRepository userTrainingRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private UserTrainingService userTrainingService;

    private User user;
    private Training training;
    private UserTrainingDTO userTrainingDTO;

    @BeforeEach
    void setUp() {
        // Clean up database
        userTrainingRepository.deleteAll();

        // Create test data
        UserDTO userDTO = new UserDTO("testId", "John", "Doe", "Ship123", Language.ENGLISH);
        user = userService.save(userDTO);

        TrainingDTO trainingDTO = new TrainingDTO(
                Collections.singletonMap(Language.ENGLISH, "Test Training"),
                Collections.singletonMap(Language.ENGLISH, "Test Description"),
                true,
                null,
                null
        );
        training = trainingService.save(trainingDTO);

        userTrainingDTO = new UserTrainingDTO(training.getId(), user.getId(), false);
    }

    @AfterEach
    void tearDown() {
        // Clean up database
        userTrainingRepository.deleteAll();
    }

    @Test
    void save_ShouldThrowException_WhenUserIdIsMissing() {
        UserTrainingDTO invalidDTO = new UserTrainingDTO(training.getId(), 0, false);
        assertThrows(MissingArgumentException.class, () -> userTrainingService.save(invalidDTO));
    }

    @Test
    void save_ShouldThrowException_WhenTrainingIdIsMissing() {
        UserTrainingDTO invalidDTO = new UserTrainingDTO(0, user.getId(), false);
        assertThrows(MissingArgumentException.class, () -> userTrainingService.save(invalidDTO));
    }

    @Test
    void save_ShouldSaveUserTraining_WhenValidDTOIsProvided() {
        userTrainingService.save(userTrainingDTO);

        List<UserTraining> savedUserTrainings = userTrainingRepository.findAll();
        assertFalse(savedUserTrainings.isEmpty());
        UserTraining savedUserTraining = savedUserTrainings.get(0);
        assertEquals(user.getId(), savedUserTraining.getUser().getId());
        assertEquals(training.getId(), savedUserTraining.getTraining().getId());
    }

    @Test
    void findById_ShouldReturnUserTraining_WhenItExists() {
        userTrainingService.save(userTrainingDTO);
        UserTraining savedUserTraining = userTrainingRepository.findAll().get(0);
        UserTraining foundUserTraining = userTrainingService.findById(savedUserTraining.getId());

        assertNotNull(foundUserTraining);
        assertEquals(savedUserTraining.getId(), foundUserTraining.getId());
        assertEquals(user.getId(), foundUserTraining.getUser().getId());
        assertEquals(training.getId(), foundUserTraining.getTraining().getId());
    }

    @Test
    void findById_ShouldThrowException_WhenUserTrainingNotFound() {
        assertThrows(NotFoundException.class, () -> userTrainingService.findById(1));
    }

    @Test
    void findAll_ShouldReturnListOfUserTrainings() {
        userTrainingService.save(userTrainingDTO);
        List<UserTraining> userTrainings = userTrainingService.findAll();

        assertFalse(userTrainings.isEmpty());
        assertEquals(1, userTrainings.size());
    }
}
