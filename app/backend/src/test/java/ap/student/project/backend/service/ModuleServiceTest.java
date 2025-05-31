package ap.student.project.backend.service;

import ap.student.project.backend.dao.ContentRepository;
import ap.student.project.backend.dao.ModuleRepository;
import ap.student.project.backend.dao.QuestionOptionRepository;
import ap.student.project.backend.dao.QuestionRepository;
import ap.student.project.backend.dto.ContentDTO;
import ap.student.project.backend.dto.ModuleDTO;
import ap.student.project.backend.dto.QuestionDTO;
import ap.student.project.backend.dto.TrainingDTO;
import ap.student.project.backend.entity.Content;
import ap.student.project.backend.entity.ContentType;
import ap.student.project.backend.entity.Language;
import ap.student.project.backend.entity.Module;
import ap.student.project.backend.entity.Question;
import ap.student.project.backend.entity.QuestionOption;
import ap.student.project.backend.entity.Training;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @Autowired
    private ContentRepository contentRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionOptionRepository questionOptionRepository;

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

    @Test
    void getModuleById_ShouldThrowException_WhenModuleNotFound() {
        assertThrows(NotFoundException.class, () -> moduleService.getModuleById(999));
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

    @Test
    void getAllModules_ShouldReturnAllModules() {
        // Save two modules
        moduleService.save(moduleDTO);
        
        Map<Language, String> secondModuleTitles = new HashMap<>();
        secondModuleTitles.put(Language.ENGLISH, "Second Test Module");
        ModuleDTO secondModuleDTO = new ModuleDTO(secondModuleTitles, null, null, trainingId);
        moduleService.save(secondModuleDTO);

        List<Module> allModules = moduleService.getAllModules();
        
        assertTrue(allModules.size() >= 2);
        assertTrue(allModules.stream().anyMatch(m -> 
            m.getTitle().get(Language.ENGLISH).equals("Test Module")));
        assertTrue(allModules.stream().anyMatch(m -> 
            m.getTitle().get(Language.ENGLISH).equals("Second Test Module")));
    }

    @Test
    void updateModule_ShouldHandleNullTitleAndDescription() {
        Module savedModule = moduleService.save(moduleDTO);
        int moduleId = savedModule.getId();

        // Create DTO with null title and description
        ModuleDTO updateDTO = new ModuleDTO(null, null, null, trainingId);
        
        moduleService.updateModule(moduleId, updateDTO);
        
        Module updatedModule = moduleService.getModuleById(moduleId);
        // Original values should remain unchanged
        assertEquals("Test Module", updatedModule.getTitle().get(Language.ENGLISH));
        assertEquals("Test Module Description", updatedModule.getDescription().get(Language.ENGLISH));
    }

    @Test
    void updateModule_ShouldUpdateDescriptionOnly() {
        Module savedModule = moduleService.save(moduleDTO);
        int moduleId = savedModule.getId();

        Map<Language, String> updatedDescriptions = new HashMap<>();
        updatedDescriptions.put(Language.ENGLISH, "Updated Description");

        ModuleDTO updateDTO = new ModuleDTO(null, updatedDescriptions, null, trainingId);
        
        moduleService.updateModule(moduleId, updateDTO);
        
        Module updatedModule = moduleService.getModuleById(moduleId);
        assertEquals("Test Module", updatedModule.getTitle().get(Language.ENGLISH)); // unchanged
        assertEquals("Updated Description", updatedModule.getDescription().get(Language.ENGLISH));
    }

    @Test
    void deleteModule_ShouldDeleteExistingModule() {
        Module savedModule = moduleService.save(moduleDTO);
        int moduleId = savedModule.getId();
        
        long countBefore = moduleRepository.count();
        moduleService.deleteModule(moduleId);
        long countAfter = moduleRepository.count();
        
        assertEquals(countBefore - 1, countAfter);
        assertThrows(NotFoundException.class, () -> moduleService.getModuleById(moduleId));
    }

    @Test
    void deleteModule_ShouldThrowException_WhenModuleNotFound() {
        assertThrows(NotFoundException.class, () -> moduleService.deleteModule(999));
    }

    @Test
    void addContent_ShouldAddContentToModule() {
        Module savedModule = moduleService.save(moduleDTO);
        int moduleId = savedModule.getId();

        Map<Language, String> contentReference = new HashMap<>();
        contentReference.put(Language.ENGLISH, "Test content reference");
        ContentDTO contentDTO = new ContentDTO(ContentType.TEXT, contentReference);

        long contentCountBefore = contentRepository.count();
        Content savedContent = moduleService.addContent(moduleId, contentDTO);
        long contentCountAfter = contentRepository.count();

        assertEquals(contentCountBefore + 1, contentCountAfter);
        assertEquals(ContentType.TEXT, savedContent.getContentType());
        assertEquals("Test content reference", savedContent.getReference().get(Language.ENGLISH));
        assertEquals(moduleId, savedContent.getModule().getId());
    }

    @Test
    void addContent_ShouldThrowException_WhenModuleNotFound() {
        Map<Language, String> contentReference = new HashMap<>();
        contentReference.put(Language.ENGLISH, "Test content reference");
        ContentDTO contentDTO = new ContentDTO(ContentType.TEXT, contentReference);

        assertThrows(NotFoundException.class, () -> moduleService.addContent(999, contentDTO));
    }

    @Test
    void updateContent_ShouldUpdateExistingContent() {
        Module savedModule = moduleService.save(moduleDTO);
        int moduleId = savedModule.getId();

        Map<Language, String> originalReference = new HashMap<>();
        originalReference.put(Language.ENGLISH, "Original reference");
        ContentDTO originalContentDTO = new ContentDTO(ContentType.TEXT, originalReference);
        Content savedContent = moduleService.addContent(moduleId, originalContentDTO);

        Map<Language, String> updatedReference = new HashMap<>();
        updatedReference.put(Language.ENGLISH, "Updated reference");
        ContentDTO updatedContentDTO = new ContentDTO(ContentType.VIDEO, updatedReference);

        Content updatedContent = moduleService.updateContent(moduleId, savedContent.getId(), updatedContentDTO);

        assertEquals(ContentType.VIDEO, updatedContent.getContentType());
        assertEquals("Updated reference", updatedContent.getReference().get(Language.ENGLISH));
    }

    @Test
    void updateContent_ShouldThrowException_WhenModuleNotFound() {
        Map<Language, String> contentReference = new HashMap<>();
        contentReference.put(Language.ENGLISH, "Test reference");
        ContentDTO contentDTO = new ContentDTO(ContentType.TEXT, contentReference);

        assertThrows(NotFoundException.class, () -> moduleService.updateContent(999, 1, contentDTO));
    }

    @Test
    void updateContent_ShouldThrowException_WhenContentNotFound() {
        Module savedModule = moduleService.save(moduleDTO);
        int moduleId = savedModule.getId();

        Map<Language, String> contentReference = new HashMap<>();
        contentReference.put(Language.ENGLISH, "Test reference");
        ContentDTO contentDTO = new ContentDTO(ContentType.TEXT, contentReference);

        assertThrows(NotFoundException.class, () -> moduleService.updateContent(moduleId, 999, contentDTO));
    }

    @Test
    void deleteContent_ShouldDeleteExistingContent() {
        Module savedModule = moduleService.save(moduleDTO);
        int moduleId = savedModule.getId();

        Map<Language, String> contentReference = new HashMap<>();
        contentReference.put(Language.ENGLISH, "Test content reference");
        ContentDTO contentDTO = new ContentDTO(ContentType.TEXT, contentReference);
        Content savedContent = moduleService.addContent(moduleId, contentDTO);

        long countBefore = contentRepository.count();
        moduleService.deleteContent(moduleId, savedContent.getId());
        long countAfter = contentRepository.count();

        assertEquals(countBefore - 1, countAfter);
        assertFalse(contentRepository.existsById(savedContent.getId()));
    }

    @Test
    void deleteContent_ShouldThrowException_WhenModuleNotFound() {
        assertThrows(NotFoundException.class, () -> moduleService.deleteContent(999, 1));
    }

    @Test
    void deleteContent_ShouldThrowException_WhenContentNotFound() {
        Module savedModule = moduleService.save(moduleDTO);
        int moduleId = savedModule.getId();

        assertThrows(NotFoundException.class, () -> moduleService.deleteContent(moduleId, 999));
    }

    @Test
    void addQuestion_ShouldAddQuestionToModule() {
        Module savedModule = moduleService.save(moduleDTO);
        int moduleId = savedModule.getId();

        Map<Language, String> questionText = new HashMap<>();
        questionText.put(Language.ENGLISH, "What is the answer?");

        Map<Language, String> option1Text = new HashMap<>();
        option1Text.put(Language.ENGLISH, "Option 1");
        Map<Language, String> option2Text = new HashMap<>();
        option2Text.put(Language.ENGLISH, "Option 2");

        List<QuestionOption> options = new ArrayList<>();
        options.add(new QuestionOption(option1Text, true, null));
        options.add(new QuestionOption(option2Text, false, null));

        QuestionDTO questionDTO = new QuestionDTO(questionText, options);

        long questionCountBefore = questionRepository.count();
        long optionCountBefore = questionOptionRepository.count();

        Question savedQuestion = moduleService.addQuestion(moduleId, questionDTO);

        long questionCountAfter = questionRepository.count();
        long optionCountAfter = questionOptionRepository.count();

        assertEquals(questionCountBefore + 1, questionCountAfter);
        assertEquals(optionCountBefore + 2, optionCountAfter);
        assertEquals("What is the answer?", savedQuestion.getText().get(Language.ENGLISH));
        assertEquals(2, savedQuestion.getQuestionOptions().size());
        assertEquals(moduleId, savedQuestion.getModule().getId());

        // Verify options are properly linked
        assertTrue(savedQuestion.getQuestionOptions().stream()
            .allMatch(option -> option.getQuestion().equals(savedQuestion)));
    }

    @Test
    void addQuestion_ShouldThrowException_WhenModuleNotFound() {
        Map<Language, String> questionText = new HashMap<>();
        questionText.put(Language.ENGLISH, "What is the answer?");
        QuestionDTO questionDTO = new QuestionDTO(questionText, new ArrayList<>());

        assertThrows(NotFoundException.class, () -> moduleService.addQuestion(999, questionDTO));
    }

    @Test
    void getQuestionsByModuleId_ShouldThrowException_WhenModuleNotFound() {
        assertThrows(NotFoundException.class, () -> moduleService.getQuestionsByModuleId(999));
    }

    @Test
    void deleteQuestions_ShouldThrowException_WhenModuleNotFound() {
        assertThrows(NotFoundException.class, () -> moduleService.deleteQuestions(999));
    }

    @Test
    void save_ShouldHandleNullTitleAndDescription() {
        ModuleDTO dtoWithNulls = new ModuleDTO(null, null, null, trainingId);
        
        Module savedModule = moduleService.save(dtoWithNulls);
        
        assertNotNull(savedModule);
        assertEquals(trainingId, savedModule.getTraining().getId());
        // Title and description can be null
        assertNull(savedModule.getTitle());
        assertNull(savedModule.getDescription());
    }

    @Test
    void updateModule_ShouldInitializeNullMaps() {
        // Create a module with null title and description
        ModuleDTO dtoWithNulls = new ModuleDTO(null, null, null, trainingId);
        Module savedModule = moduleService.save(dtoWithNulls);
        int moduleId = savedModule.getId();

        // Update with actual values
        Map<Language, String> newTitle = new HashMap<>();
        newTitle.put(Language.ENGLISH, "New Title");
        Map<Language, String> newDescription = new HashMap<>();
        newDescription.put(Language.ENGLISH, "New Description");

        ModuleDTO updateDTO = new ModuleDTO(newTitle, newDescription, null, trainingId);
        
        moduleService.updateModule(moduleId, updateDTO);
        
        Module updatedModule = moduleService.getModuleById(moduleId);
        assertEquals("New Title", updatedModule.getTitle().get(Language.ENGLISH));
        assertEquals("New Description", updatedModule.getDescription().get(Language.ENGLISH));
    }
}

