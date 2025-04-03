package ap.student.project.backend.service;

import ap.student.project.backend.dao.ModuleRepository;
import ap.student.project.backend.dto.ModuleDTO;
import ap.student.project.backend.dto.VideoDTO;
import ap.student.project.backend.entity.Module;
import ap.student.project.backend.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    private ModuleRepository moduleRepository;

    @InjectMocks
    private ModuleService moduleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTrainings() {
        Module t1 = new Module();
        Module t2 = new Module();
        when(moduleRepository.findAll()).thenReturn(Arrays.asList(t1, t2));

        List<Module> modules = moduleService.getAllTrainings();

        assertEquals(2, modules.size());
        verify(moduleRepository, times(1)).findAll();
    }

    @Test
    void testGetTrainingById_Found() {
        Module module = new Module();
        module.setId(1);
        when(moduleRepository.findById(1)).thenReturn(Optional.of(module));

        Module result = moduleService.getTrainingById(1);

        assertEquals(1, result.getId());
    }

    @Test
    void testGetTrainingById_NotFound() {
        when(moduleRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> moduleService.getTrainingById(1));
    }

    @Test
    void testDeleteTrainingById_Found() {
        when(moduleRepository.existsById(1)).thenReturn(true);

        moduleService.deleteTrainingById(1);

        verify(moduleRepository).deleteById(1);
    }

    @Test
    void testDeleteTrainingById_NotFound() {
        when(moduleRepository.existsById(1)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> moduleService.deleteTrainingById(1));
    }

    @Test
    void testUpdateTraining_NotFound() {
        when(moduleRepository.findById(1)).thenReturn(Optional.empty());

        ModuleDTO dto = new ModuleDTO(null, null, null, 1);
        assertThrows(NotFoundException.class, () -> moduleService.updateTraining(1, dto));
    }

    @Test
    void testAddVideo_NotFound() {
        when(moduleRepository.findById(1)).thenReturn(Optional.empty());

        VideoDTO videoDTO = new VideoDTO(null, null);
        assertThrows(NotFoundException.class, () -> moduleService.addVideo(1, videoDTO));
    }
}
