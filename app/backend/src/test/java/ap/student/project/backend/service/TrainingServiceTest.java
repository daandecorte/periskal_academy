package ap.student.project.backend.service;

import ap.student.project.backend.dao.TrainingRepository;
import ap.student.project.backend.dto.TrainingDTO;
import ap.student.project.backend.dto.VideoDTO;
import ap.student.project.backend.entity.Language;
import ap.student.project.backend.entity.Training;
import ap.student.project.backend.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingServiceTest {

    @Mock
    private TrainingRepository trainingRepository;

    @InjectMocks
    private TrainingService trainingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTrainings() {
        Training t1 = new Training();
        Training t2 = new Training();
        when(trainingRepository.findAll()).thenReturn(Arrays.asList(t1, t2));

        List<Training> trainings = trainingService.getAllTrainings();

        assertEquals(2, trainings.size());
        verify(trainingRepository, times(1)).findAll();
    }

    @Test
    void testGetTrainingById_Found() {
        Training training = new Training();
        training.setId(1);
        when(trainingRepository.findById(1)).thenReturn(Optional.of(training));

        Training result = trainingService.getTrainingById(1);

        assertEquals(1, result.getId());
    }

    @Test
    void testGetTrainingById_NotFound() {
        when(trainingRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> trainingService.getTrainingById(1));
    }

    @Test
    void testDeleteTrainingById_Found() {
        when(trainingRepository.existsById(1)).thenReturn(true);

        trainingService.deleteTrainingById(1);

        verify(trainingRepository).deleteById(1);
    }

    @Test
    void testDeleteTrainingById_NotFound() {
        when(trainingRepository.existsById(1)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> trainingService.deleteTrainingById(1));
    }

    @Test
    void testUpdateTraining_NotFound() {
        when(trainingRepository.findById(1)).thenReturn(Optional.empty());

        TrainingDTO dto = new TrainingDTO(null, null, null, 1);
        assertThrows(NotFoundException.class, () -> trainingService.updateTraining(1, dto));
    }

    @Test
    void testAddVideo_NotFound() {
        when(trainingRepository.findById(1)).thenReturn(Optional.empty());

        VideoDTO videoDTO = new VideoDTO(null, null);
        assertThrows(NotFoundException.class, () -> trainingService.addVideo(1, videoDTO));
    }
}
