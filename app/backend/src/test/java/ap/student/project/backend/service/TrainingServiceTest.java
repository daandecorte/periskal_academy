package ap.student.project.backend.service;
import ap.student.project.backend.dao.TrainingRepository;
import ap.student.project.backend.dto.TrainingDTO;
import ap.student.project.backend.entity.Training;
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
class TrainingServiceTest {

    @Mock
    private TrainingRepository trainingRepository;

    @InjectMocks
    private TrainingService trainingService;

    private TrainingDTO trainingDTO;
    private Training training;

    @BeforeEach
    void setUp() {
        trainingDTO = new TrainingDTO(null,null, false, null, null); // Assuming a name field in DTO
        training = new Training();
    }

    @Test
    void save_ShouldSaveTraining_WhenValidDTOIsProvided() {
        trainingService.save(trainingDTO);
        verify(trainingRepository, times(1)).save(any(Training.class));
    }

    @Test
    void findById_ShouldThrowException_WhenTrainingNotFound() {
        when(trainingRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> trainingService.findById(1));
    }

    @Test
    void findById_ShouldReturnTraining_WhenFound() {
        when(trainingRepository.findById(1)).thenReturn(Optional.of(training));
        Training result = trainingService.findById(1);
        assertNotNull(result);
        verify(trainingRepository, times(1)).findById(1);
    }

    @Test
    void update_ShouldThrowException_WhenTrainingNotFound() {
        when(trainingRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> trainingService.update(1, trainingDTO));
    }

    @Test
    void update_ShouldSaveUpdatedTraining_WhenFound() {
        when(trainingRepository.findById(1)).thenReturn(Optional.of(training));

        trainingService.update(1, trainingDTO);

        verify(trainingRepository, times(1)).save(any(Training.class));
    }

    @Test
    void findAll_ShouldReturnListOfTrainings() {
        when(trainingRepository.findAll()).thenReturn(List.of(training));

        List<Training> result = trainingService.findAll();
        assertEquals(1, result.size());
        verify(trainingRepository, times(1)).findAll();
    }

}