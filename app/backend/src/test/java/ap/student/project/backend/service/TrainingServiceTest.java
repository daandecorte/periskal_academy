package ap.student.project.backend.service;

import ap.student.project.backend.dao.TrainingRepository;
import ap.student.project.backend.dto.TrainingDTO;
import ap.student.project.backend.entity.Language;
import ap.student.project.backend.entity.Training;
import ap.student.project.backend.exceptions.NotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private TrainingDTO multiLanguageTrainingDTO;

    @BeforeEach
    void setUp() {
        // Clean up database
        trainingRepository.deleteAll();

        // Create basic test data with mutable maps
        Map<Language, String> basicTitles = new HashMap<>();
        basicTitles.put(Language.ENGLISH, "Test Training");

        Map<Language, String> basicDescriptions = new HashMap<>();
        basicDescriptions.put(Language.ENGLISH, "Test Description");

        trainingDTO = new TrainingDTO(
                basicTitles,
                basicDescriptions,
                true,
                null,
                null
        );

        // Create multi-language test data
        Map<Language, String> multiLangTitles = new HashMap<>();
        multiLangTitles.put(Language.ENGLISH, "Multi-Language Training");
        multiLangTitles.put(Language.DUTCH, "Meertalige Training");

        Map<Language, String> multiLangDescriptions = new HashMap<>();
        multiLangDescriptions.put(Language.ENGLISH, "Training in multiple languages");
        multiLangDescriptions.put(Language.DUTCH, "Training in meerdere talen");

        multiLanguageTrainingDTO = new TrainingDTO(
                multiLangTitles,
                multiLangDescriptions,
                false,
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
    void save_ShouldSaveMultiLanguageTraining_WhenValidMultiLanguageDTOIsProvided() {
        Training savedTraining = trainingService.save(multiLanguageTrainingDTO);

        assertNotNull(savedTraining);
        assertNotNull(savedTraining.getId());
        assertEquals("Multi-Language Training", savedTraining.getTitle().get(Language.ENGLISH));
        assertEquals("Meertalige Training", savedTraining.getTitle().get(Language.DUTCH));
        assertEquals("Training in multiple languages", savedTraining.getDescription().get(Language.ENGLISH));
        assertEquals("Training in meerdere talen", savedTraining.getDescription().get(Language.DUTCH));
        assertFalse(savedTraining.isActive());
    }

    @Test
    void save_ShouldSaveInactiveTraining_WhenIsActiveFalse() {
        Map<Language, String> inactiveTitles = new HashMap<>();
        inactiveTitles.put(Language.ENGLISH, "Inactive Training");

        Map<Language, String> inactiveDescriptions = new HashMap<>();
        inactiveDescriptions.put(Language.ENGLISH, "Inactive Description");

        TrainingDTO inactiveTrainingDTO = new TrainingDTO(
                inactiveTitles,
                inactiveDescriptions,
                false,
                null,
                null
        );

        Training savedTraining = trainingService.save(inactiveTrainingDTO);

        assertNotNull(savedTraining);
        assertFalse(savedTraining.isActive());
        assertEquals("Inactive Training", savedTraining.getTitle().get(Language.ENGLISH));
    }

    @Test
    void save_ShouldGenerateUniqueIds_WhenMultipleTrainingsSaved() {
        Training firstTraining = trainingService.save(trainingDTO);
        Training secondTraining = trainingService.save(multiLanguageTrainingDTO);

        assertNotNull(firstTraining.getId());
        assertNotNull(secondTraining.getId());
        assertNotEquals(firstTraining.getId(), secondTraining.getId());
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
        assertEquals(savedTraining.getDescription(), foundTraining.getDescription());
        assertEquals(savedTraining.isActive(), foundTraining.isActive());
    }

    @Test
    void findById_ShouldReturnCorrectTraining_WhenMultipleTrainingsExist() {
        Training firstTraining = trainingService.save(trainingDTO);
        Training secondTraining = trainingService.save(multiLanguageTrainingDTO);

        Training foundFirst = trainingService.findById(firstTraining.getId());
        Training foundSecond = trainingService.findById(secondTraining.getId());

        assertEquals("Test Training", foundFirst.getTitle().get(Language.ENGLISH));
        assertEquals("Multi-Language Training", foundSecond.getTitle().get(Language.ENGLISH));
        assertTrue(foundFirst.isActive());
        assertFalse(foundSecond.isActive());
    }

    @Test
    void findById_ShouldThrowException_WhenNegativeIdProvided() {
        assertThrows(NotFoundException.class, () -> trainingService.findById(-1));
    }

    @Test
    void findById_ShouldThrowException_WhenZeroIdProvided() {
        assertThrows(NotFoundException.class, () -> trainingService.findById(0));
    }

    @Test
    void update_ShouldThrowException_WhenTrainingNotFound() {
        assertThrows(NotFoundException.class, () -> trainingService.update(9999, trainingDTO));
    }

    @Test
    void update_ShouldSaveUpdatedTraining_WhenFound() {
        Training savedTraining = trainingService.save(trainingDTO);

        // Create updated DTO with new title using mutable maps
        Map<Language, String> updatedTitles = new HashMap<>();
        updatedTitles.put(Language.ENGLISH, "Updated Training");

        Map<Language, String> updatedDescriptions = new HashMap<>();
        updatedDescriptions.put(Language.ENGLISH, "Updated Description");

        TrainingDTO updatedDTO = new TrainingDTO(
                updatedTitles,
                updatedDescriptions,
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
    void update_ShouldUpdateFromActiveToInactive_WhenStatusChanged() {
        Training savedTraining = trainingService.save(trainingDTO);
        assertTrue(savedTraining.isActive());

        // Create mutable copies of the maps
        Map<Language, String> titlesCopy = new HashMap<>(trainingDTO.title());
        Map<Language, String> descriptionsCopy = new HashMap<>(trainingDTO.description());

        TrainingDTO inactiveDTO = new TrainingDTO(
                titlesCopy,
                descriptionsCopy,
                false,
                null,
                null
        );

        trainingService.update(savedTraining.getId(), inactiveDTO);

        Training updatedTraining = trainingRepository.findById(savedTraining.getId()).orElseThrow();
        assertFalse(updatedTraining.isActive());
    }

    @Test
    void update_ShouldUpdateFromInactiveToActive_WhenStatusChanged() {
        Training savedTraining = trainingService.save(multiLanguageTrainingDTO);
        assertFalse(savedTraining.isActive());

        // Create mutable copies of the maps
        Map<Language, String> titlesCopy = new HashMap<>(multiLanguageTrainingDTO.title());
        Map<Language, String> descriptionsCopy = new HashMap<>(multiLanguageTrainingDTO.description());

        TrainingDTO activeDTO = new TrainingDTO(
                titlesCopy,
                descriptionsCopy,
                true,
                null,
                null
        );

        trainingService.update(savedTraining.getId(), activeDTO);

        Training updatedTraining = trainingRepository.findById(savedTraining.getId()).orElseThrow();
        assertTrue(updatedTraining.isActive());
    }

    @Test
    void update_ShouldUpdateMultiLanguageTitles_WhenNewLanguagesProvided() {
        Training savedTraining = trainingService.save(trainingDTO);

        Map<Language, String> newTitles = new HashMap<>();
        newTitles.put(Language.ENGLISH, "Updated English Title");
        newTitles.put(Language.DUTCH, "Nederlandse Titel");

        Map<Language, String> newDescriptions = new HashMap<>();
        newDescriptions.put(Language.ENGLISH, "Updated English Description");
        newDescriptions.put(Language.DUTCH, "Nederlandse Beschrijving");

        TrainingDTO updatedDTO = new TrainingDTO(
                newTitles,
                newDescriptions,
                trainingDTO.isActive(),
                null,
                null
        );

        trainingService.update(savedTraining.getId(), updatedDTO);

        Training updatedTraining = trainingRepository.findById(savedTraining.getId()).orElseThrow();
        assertEquals("Updated English Title", updatedTraining.getTitle().get(Language.ENGLISH));
        assertEquals("Nederlandse Titel", updatedTraining.getTitle().get(Language.DUTCH));
        assertEquals("Updated English Description", updatedTraining.getDescription().get(Language.ENGLISH));
        assertEquals("Nederlandse Beschrijving", updatedTraining.getDescription().get(Language.DUTCH));
    }

    @Test
    void update_ShouldThrowException_WhenNegativeIdProvided() {
        assertThrows(NotFoundException.class, () -> trainingService.update(-1, trainingDTO));
    }

    @Test
    void update_ShouldThrowException_WhenZeroIdProvided() {
        assertThrows(NotFoundException.class, () -> trainingService.update(0, trainingDTO));
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenNoTrainingsExist() {
        List<Training> result = trainingService.findAll();
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_ShouldReturnListOfTrainings() {
        trainingService.save(trainingDTO);
        List<Training> result = trainingService.findAll();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void findAll_ShouldReturnAllTrainings_WhenMultipleTrainingsExist() {
        trainingService.save(trainingDTO);
        trainingService.save(multiLanguageTrainingDTO);

        TrainingDTO thirdTraining = new TrainingDTO(
                Map.of(Language.ENGLISH, "Third Training"),
                Map.of(Language.ENGLISH, "Third Description"),
                true,
                null,
                null
        );
        trainingService.save(thirdTraining);

        List<Training> result = trainingService.findAll();

        assertEquals(3, result.size());

        // Verify all trainings are present by checking titles
        List<String> englishTitles = result.stream()
                .map(training -> training.getTitle().get(Language.ENGLISH))
                .toList();

        assertTrue(englishTitles.contains("Test Training"));
        assertTrue(englishTitles.contains("Multi-Language Training"));
        assertTrue(englishTitles.contains("Third Training"));
    }

    @Test
    void findAll_ShouldReturnBothActiveAndInactiveTrainings() {
        trainingService.save(trainingDTO); // active
        trainingService.save(multiLanguageTrainingDTO); // inactive

        List<Training> result = trainingService.findAll();

        assertEquals(2, result.size());

        long activeCount = result.stream().mapToLong(training -> training.isActive() ? 1 : 0).sum();
        long inactiveCount = result.stream().mapToLong(training -> !training.isActive() ? 1 : 0).sum();

        assertEquals(1, activeCount);
        assertEquals(1, inactiveCount);
    }

    @Test
    void integration_ShouldSaveUpdateAndRetrieveTraining_WhenComplexWorkflow() {
        // Save initial training
        Training savedTraining = trainingService.save(trainingDTO);
        assertNotNull(savedTraining.getId());
        assertTrue(savedTraining.isActive());

        // Update the training
        Map<Language, String> integrationTitles = new HashMap<>();
        integrationTitles.put(Language.ENGLISH, "Integration Test Training");

        Map<Language, String> integrationDescriptions = new HashMap<>();
        integrationDescriptions.put(Language.ENGLISH, "Integration Test Description");

        TrainingDTO updatedDTO = new TrainingDTO(
                integrationTitles,
                integrationDescriptions,
                false,
                null,
                null
        );
        trainingService.update(savedTraining.getId(), updatedDTO);

        // Retrieve and verify
        Training retrievedTraining = trainingService.findById(savedTraining.getId());
        assertEquals("Integration Test Training", retrievedTraining.getTitle().get(Language.ENGLISH));
        assertEquals("Integration Test Description", retrievedTraining.getDescription().get(Language.ENGLISH));
        assertFalse(retrievedTraining.isActive());

        // Verify it appears in findAll
        List<Training> allTrainings = trainingService.findAll();
        assertEquals(1, allTrainings.size());
        assertEquals(retrievedTraining.getId(), allTrainings.get(0).getId());
    }
}