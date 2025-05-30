package ap.student.project.backend.service;

import ap.student.project.backend.dao.ModuleRepository;
import ap.student.project.backend.dto.ModuleDTO;
import ap.student.project.backend.dto.TrainingDTO;
import ap.student.project.backend.entity.Language;
import ap.student.project.backend.entity.Module;
import ap.student.project.backend.entity.Training;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ModuleServiceTest {

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private TrainingService trainingService;

    @Autowired
    private ModuleService moduleService;

    private ModuleDTO moduleDTO;
    private Training testTraining;
    private int trainingId;

    @BeforeEach
    void setUp() {
        // Create a test training first
        Map<Language, String> titles = new HashMap<>();
        titles.put(Language.ENGLISH, "Test Training");

        Map<Language, String> descriptions = new HashMap<>();
        descriptions.put(Language.ENGLISH, "Test Description");

        Training training = new Training(titles, descriptions, true, null, null);
        testTraining = trainingService.save(new TrainingDTO(titles, descriptions, true, null, null));
        trainingId = testTraining.getId();

        // Create module DTO for testing
        Map<Language, String> moduleTitles = new HashMap<>();
        moduleTitles.put(Language.ENGLISH, "Test Module");

        Map<Language, String> moduleDescriptions = new HashMap<>();
        moduleDescriptions.put(Language.ENGLISH, "Test Module Description");

        moduleDTO = new ModuleDTO(moduleTitles, moduleDescriptions, null, trainingId);
    }

    @Test
    void save_ShouldThrowException_WhenTrainingIdIsZero() {
        ModuleDTO invalidDTO = new ModuleDTO(null, null, null, 0);
        assertThrows(MissingArgumentException.class, () -> moduleService.save(invalidDTO));
    }

    @Test
    void save_ShouldSaveModule_WhenValidDTOIsProvided() {
        long countBefore = moduleRepository.count();
        moduleService.save(moduleDTO);

        // Verify module was saved
        long countAfter = moduleRepository.count();
        assertEquals(countBefore + 1, countAfter);

        // Verify the module is associated with the training
        Module savedModule = moduleRepository.findAll().stream()
                .filter(m -> m.getTraining().getId() == trainingId)
                .findFirst()
                .orElse(null);

        assertNotNull(savedModule);
        assertEquals(moduleDTO.title().get(Language.ENGLISH), savedModule.getTitle().get(Language.ENGLISH));
    }

    void getModuleById_ShouldThrowException_WhenModuleNotFound() {
        assertThrows(NotFoundException.class, () -> moduleService.getModuleById(1));
    }

    @Test
    void getModuleById_ShouldReturnModule_WhenFound() {
        moduleService.save(moduleDTO);

        Module savedModule = moduleRepository.findAll().stream()
                .filter(m -> m.getTraining().getId() == trainingId)
                .findFirst()
                .orElse(null);

        assertNotNull(savedModule);

        Module foundModule = moduleService.getModuleById(savedModule.getId());

        assertNotNull(foundModule);
        assertEquals(savedModule.getId(), foundModule.getId());
    }

    @Test
    void updateModule_ShouldThrowException_WhenModuleNotFound() {
        assertThrows(NotFoundException.class, () -> moduleService.updateModule(1, moduleDTO));
    }

    @Test
    void updateModule_ShouldSaveUpdatedModule_WhenFound() {
        moduleService.save(moduleDTO);

        Module savedModule = moduleRepository.findAll().stream()
                .filter(m -> m.getTraining().getId() == trainingId)
                .findFirst()
                .orElse(null);

        assertNotNull(savedModule);
        int moduleId = savedModule.getId();

        Map<Language, String> updatedTitles = new HashMap<>();
        updatedTitles.put(Language.ENGLISH, "Updated Module Title");

        ModuleDTO updatedDTO = new ModuleDTO(
                updatedTitles,
                moduleDTO.description(),
                null,
                trainingId
        );

        moduleService.updateModule(moduleId, updatedDTO);

        Module updatedModule = moduleService.getModuleById(moduleId);
        assertEquals("Updated Module Title", updatedModule.getTitle().get(Language.ENGLISH));
    }
}
