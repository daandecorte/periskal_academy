package ap.student.project.backend.service;

import ap.student.project.backend.dao.UserTrainingRepository;
import ap.student.project.backend.dto.UserTrainingDTO;
import ap.student.project.backend.entity.Training;
import ap.student.project.backend.entity.User;
import ap.student.project.backend.entity.UserTraining;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserModuleServiceTest {

    private UserTrainingRepository userTrainingRepository;
    private UserService userService;
    private TrainingService trainingService;
    private UserTrainingService userTrainingService;

    @BeforeEach
    void setUp() {
        userTrainingRepository = mock(UserTrainingRepository.class);
        userService = mock(UserService.class);
        trainingService = mock(TrainingService.class);
        userTrainingService = new UserTrainingService(userTrainingRepository, userService, trainingService);
    }

    @Test
    void save_shouldSaveUserModule() {
        User user = new User();
        user.setId(1);
        Training training = new Training();
        training.setId(2);
        UserTrainingDTO dto = new UserTrainingDTO(null, 2, 1);

        when(userService.findById(1)).thenReturn(user);
        when(trainingService.findById(2)).thenReturn(training);

        userTrainingService.save(dto);

        ArgumentCaptor<UserTraining> captor = ArgumentCaptor.forClass(UserTraining.class);
        verify(userTrainingRepository, times(1)).save(captor.capture());
        UserTraining savedUserTraining = captor.getValue();

        assertEquals(1, savedUserTraining.getUser().getId());
        assertEquals(2, savedUserTraining.getTraining().getId());
    }

    @Test
    void findAll_shouldReturnAllUserModules() {
        UserTraining um1 = new UserTraining();
        UserTraining um2 = new UserTraining();
        when(userTrainingRepository.findAll()).thenReturn(Arrays.asList(um1, um2));

        List<UserTraining> result = userTrainingService.findAll();

        assertEquals(2, result.size());
        verify(userTrainingRepository, times(1)).findAll();
    }
}
