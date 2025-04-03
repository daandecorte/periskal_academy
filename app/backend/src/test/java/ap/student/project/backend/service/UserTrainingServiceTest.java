package ap.student.project.backend.service;

import ap.student.project.backend.dao.UserTrainingRepository;
import ap.student.project.backend.dto.UserTrainingDTO;
import ap.student.project.backend.entity.Language;
import ap.student.project.backend.entity.Training;
import ap.student.project.backend.entity.User;
import ap.student.project.backend.entity.UserTraining;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserTrainingServiceTest {

    @Mock
    private UserTrainingRepository userTrainingRepository;

    @Mock
    private UserService userService;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private UserTrainingService userTrainingService;

    private UserTraining userTraining;
    private UserTrainingDTO userTrainingDTO;
    private User user;
    private Training training;

    @BeforeEach
    void setUp() {
        user = new User("testId", "John", "Doe", "Ship123", Language.ENGLISH);
        training = new Training();
        userTraining = new UserTraining();
        userTraining.setUser(user);
        userTraining.setTraining(training);
        userTrainingDTO = new UserTrainingDTO(null, 1, 1);
    }

    @Test
    void save_ShouldThrowException_WhenUserIdIsMissing() {
        UserTrainingDTO dto = new UserTrainingDTO(null, 1, 0);
        assertThrows(MissingArgumentException.class, () -> userTrainingService.save(dto));
    }

    @Test
    void save_ShouldThrowException_WhenTrainingIdIsMissing() {
        UserTrainingDTO dto = new UserTrainingDTO(null, 0, 1);
        assertThrows(MissingArgumentException.class, () -> userTrainingService.save(dto));
    }

    @Test
    void save_ShouldSaveUserTraining_WhenValidDTOIsProvided() {
        when(userService.findById(1)).thenReturn(user);
        when(trainingService.findById(1)).thenReturn(training);
        userTrainingService.save(userTrainingDTO);
        verify(userTrainingRepository, times(1)).save(any(UserTraining.class));
    }

    @Test
    void findById_ShouldReturnUserTraining_WhenItExists() {
        when(userTrainingRepository.findById(anyInt())).thenReturn(Optional.of(userTraining));
        UserTraining found = userTrainingService.findById(1);
        assertNotNull(found);
    }

    @Test
    void findById_ShouldThrowException_WhenUserTrainingNotFound() {
        when(userTrainingRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userTrainingService.findById(1));
    }

    @Test
    void findAll_ShouldReturnListOfUserTrainings() {
        when(userTrainingRepository.findAll()).thenReturn(List.of(userTraining));
        List<UserTraining> userTrainings = userTrainingService.findAll();
        assertFalse(userTrainings.isEmpty());
    }
}
