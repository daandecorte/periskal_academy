package ap.student.project.backend.service;
import ap.student.project.backend.dao.TrainingRepository;
import ap.student.project.backend.dto.TrainingDTO;
import ap.student.project.backend.entity.Language;
import ap.student.project.backend.entity.Training;
import ap.student.project.backend.exceptions.NotFoundException;
import org.springframework.transaction.annotation.Transactional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TrainingServiceTest {

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private TrainingService trainingService;

    private TrainingDTO trainingDTO;

    @BeforeEach
    void setUp() {
        // Clean up database
        trainingRepository.deleteAll();
        
        // Create test data
        trainingDTO = new TrainingDTO(
            Collections.singletonMap(Language.ENGLISH, "Test Training"),
            Collections.singletonMap(Language.ENGLISH, "Test Description"),
            true, 
            null, 
            null
        );
    }

    @AfterEach
    void tearDown() {
        // Clean up database
        trainingRepository.deleteAll();
    }

    @Test
    void save_ShouldSaveTraining_WhenValidDTOIsProvided() {
        Training savedTraining = trainingService.save(trainingDTO);
        
        assertNotNull(savedTraining);
        assertNotNull(savedTraining.getId());
        assertEquals("Test Training", savedTraining.getTitle().get(Language.ENGLISH));
        assertEquals("Test Description", savedTraining.getDescription().get(Language.ENGLISH));
        assertTrue(savedTraining.isActive());
    }
    @Test
    void findById_ShouldThrowException_WhenTrainingNotFound() {
        assertThrows(NotFoundException.class, () -> trainingService.findById(9999));
    }

    @Test
    void findById_ShouldReturnTraining_WhenFound() {
        Training savedTraining = trainingService.save(trainingDTO);
        Training foundTraining = trainingService.findById(savedTraining.getId());
        
        assertNotNull(foundTraining);
        assertEquals(savedTraining.getId(), foundTraining.getId());
        assertEquals(savedTraining.getTitle(), foundTraining.getTitle());
    }

    @Test
    void update_ShouldThrowException_WhenTrainingNotFound() {
        assertThrows(NotFoundException.class, () -> trainingService.update(9999, trainingDTO));
    }

    void update_ShouldSaveUpdatedTraining_WhenFound() {
        Training savedTraining = trainingService.save(trainingDTO);
        
        // Create updated DTO with new title
        TrainingDTO updatedDTO = new TrainingDTO(
            Collections.singletonMap(Language.ENGLISH, "Updated Training"),
            Collections.singletonMap(Language.ENGLISH, "Updated Description"),
            false, 
            null, 
            null
        );
        
        trainingService.update(savedTraining.getId(), updatedDTO);
        
        Training updatedTraining = trainingRepository.findById(savedTraining.getId()).orElseThrow();
        assertEquals("Updated Training", updatedTraining.getTitle().get(Language.ENGLISH));
        assertEquals("Updated Description", updatedTraining.getDescription().get(Language.ENGLISH));
        assertFalse(updatedTraining.isActive());
    }

    @Test
    void findAll_ShouldReturnListOfTrainings() {
        trainingService.save(trainingDTO);
        List<Training> result = trainingService.findAll();
        
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}

