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
import org.springframework.beans.BeanUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModuleServiceTest {

    @Mock
    private TrainingRepository trainingRepository;

    @InjectMocks
    private TrainingService trainingService;

    private Training training;
    private TrainingDTO trainingDTO;

    @BeforeEach
    void setUp() {
        training = new Training();
        training.setId(1);
        training.setActive(true);
        trainingDTO = new TrainingDTO(null, null, false, null, null);
    }

    @Test
    void save_ShouldSaveModule_WhenModuleDoesNotExist() {
        Training training = new Training();
        BeanUtils.copyProperties(trainingDTO, training);

        trainingService.save(trainingDTO);
        verify(trainingRepository, times(1)).save(any(Training.class));
    }

    @Test
    void findById_ShouldThrowNotFoundException_WhenModuleNotFound() {
        when(trainingRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            trainingService.findById(1);
        });

        assertEquals("Training with id 1 not found", exception.getMessage());
    }

    @Test
    void findById_ShouldReturnModule_WhenModuleExists() {
        when(trainingRepository.findById(1)).thenReturn(Optional.of(training));

        Training foundTraining = trainingService.findById(1);

        assertNotNull(foundTraining);
        assertEquals(1, foundTraining.getId());
        assertTrue(foundTraining.isActive());
    }

    @Test
    void update_ShouldThrowNotFoundException_WhenModuleNotFound() {
        when(trainingRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            trainingService.update(1, trainingDTO);
        });

        assertEquals("Training with id 1 not found", exception.getMessage());
        verify(trainingRepository, never()).save(any(Training.class));
    }

    @Test
    void update_ShouldUpdateModule_WhenModuleExists() {
        when(trainingRepository.findById(1)).thenReturn(Optional.of(training));

        trainingService.update(1, trainingDTO);

        verify(trainingRepository, times(1)).save(training);
        assertFalse(training.isActive());
    }

    @Test
    void findAll_ShouldReturnModuleList() {
        List<Training> trainings = Arrays.asList(training, new Training());
        when(trainingRepository.findAll()).thenReturn(trainings);

        List<Training> result = trainingService.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void delete_ShouldCallDeleteById() {
        trainingService.delete(1);

        verify(trainingRepository, times(1)).deleteById(1);
    }
}
