package ap.student.project.backend.service;

import ap.student.project.backend.dao.ModuleRepository;
import ap.student.project.backend.dto.ModuleDTO;
import ap.student.project.backend.dto.VideoDTO;
import ap.student.project.backend.entity.Language;
import ap.student.project.backend.entity.Module;
import ap.student.project.backend.entity.Training;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModuleServiceTest {

    @Mock
    private ModuleRepository moduleRepository;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    private ModuleService moduleService;

    private Module module;
    private ModuleDTO moduleDTO;
    private Training training;

    @BeforeEach
    void setUp() {
        moduleDTO = new ModuleDTO(null, null, null ,1); // Assuming trainingId is required
        module = mock(Module.class); // Mocking Module to avoid real object instantiation issues
        training = new Training();
    }

    @Test
    void save_ShouldThrowException_WhenTrainingIdIsZero() {
        ModuleDTO invalidDTO = new ModuleDTO(null,null, null, 0);
        assertThrows(MissingArgumentException.class, () -> moduleService.save(invalidDTO));
    }

    @Test
    void save_ShouldSaveModule_WhenValidDTOIsProvided() {
        when(trainingService.findById(1)).thenReturn(training);

        moduleService.save(moduleDTO);

        verify(trainingService, times(1)).findById(1);
        verify(moduleRepository, times(1)).save(any(Module.class));
    }

    @Test
    void getModuleById_ShouldThrowException_WhenModuleNotFound() {
        when(moduleRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> moduleService.getModuleById(1));
    }

    @Test
    void getModuleById_ShouldReturnModule_WhenFound() {
        when(moduleRepository.findById(1)).thenReturn(Optional.of(module));
        Module foundModule = moduleService.getModuleById(1);
        assertNotNull(foundModule);
    }

    @Test
    void updateModule_ShouldThrowException_WhenModuleNotFound() {
        when(moduleRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> moduleService.updateModule(1, moduleDTO));
    }

    @Test
    void updateModule_ShouldSaveUpdatedModule_WhenFound() {
        when(moduleRepository.findById(1)).thenReturn(Optional.of(module));

        moduleService.updateModule(1, moduleDTO);

        verify(moduleRepository, times(1)).save(any(Module.class));
    }

    @Test
    void addVideo_ShouldThrowException_WhenModuleNotFound() {
        when(moduleRepository.findById(1)).thenReturn(Optional.empty());
        VideoDTO videoDTO = new VideoDTO("video_url", Language.ENGLISH);
        assertThrows(NotFoundException.class, () -> moduleService.addVideo(1, videoDTO));
    }

    @Test
    void addVideo_ShouldSaveModuleWithVideo_WhenModuleExists() {
        when(moduleRepository.findById(1)).thenReturn(Optional.of(module));
        when(module.getVideoReference()).thenReturn(new HashMap<>());

        VideoDTO videoDTO = new VideoDTO("video_url", Language.ENGLISH);
        moduleService.addVideo(1, videoDTO);

        verify(module, times(1)).getVideoReference();
        verify(moduleRepository, times(1)).save(module);
    }
}
