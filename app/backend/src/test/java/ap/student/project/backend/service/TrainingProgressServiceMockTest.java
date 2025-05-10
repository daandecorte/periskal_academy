package ap.student.project.backend.service;

import ap.student.project.backend.dao.TrainingProgressRepository;
import ap.student.project.backend.dto.TrainingProgressDTO;
import ap.student.project.backend.entity.TrainingProgress;
import ap.student.project.backend.entity.UserTraining;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingProgressServiceMockTest {

    @Mock
    private TrainingProgressRepository trainingProgressRepository;

    @Mock
    private UserTrainingService userTrainingService;

    @InjectMocks
    private TrainingProgressService trainingProgressService;

    private TrainingProgressDTO trainingProgressDTO;
    private TrainingProgress trainingProgress;
    private UserTraining userTraining;

    @BeforeEach
    void setUp() {
        trainingProgressDTO = new TrainingProgressDTO(null, null, null, null, 1); // Assuming userTrainingId is required
        trainingProgress = new TrainingProgress();
        userTraining = new UserTraining();
    }

    @Test
    void save_ShouldSaveTrainingProgress_WhenValidDTOIsProvided() {
        when(userTrainingService.findById(1)).thenReturn(userTraining);

        trainingProgressService.save(trainingProgressDTO);

        verify(userTrainingService, times(1)).findById(1);
        verify(trainingProgressRepository, times(1)).save(any(TrainingProgress.class));
    }

    @Test
    void findAll_ShouldReturnListOfTrainingProgress() {
        when(trainingProgressRepository.findAll()).thenReturn(List.of(trainingProgress));

        List<TrainingProgress> result = trainingProgressService.findAll();
        assertEquals(1, result.size());
        verify(trainingProgressRepository, times(1)).findAll();
    }
}

