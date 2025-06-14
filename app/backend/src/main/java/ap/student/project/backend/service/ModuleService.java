package ap.student.project.backend.service;

import ap.student.project.backend.dao.ContentRepository;
import ap.student.project.backend.dao.ModuleRepository;
import ap.student.project.backend.dao.QuestionOptionRepository;
import ap.student.project.backend.dao.QuestionRepository;
import ap.student.project.backend.dto.ContentDTO;
import ap.student.project.backend.dto.ModuleDTO;
import ap.student.project.backend.dto.QuestionDTO;
import ap.student.project.backend.entity.Module;
import ap.student.project.backend.entity.*;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * Service class for managing training modules.
 * Handles creating, retrieving, and updating modules and their associated content and questions.
 */
@Service
public class ModuleService {
    private final ModuleRepository moduleRepository;
    private final TrainingService trainingService;
    private final ContentRepository contentRepository;
    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository questionOptionRepository;

    /**
     * Constructs a new ModuleService with the required repositories and services.
     *
     * @param moduleRepository   Repository for Module entity operations
     * @param trainingService    Service for training-related operations
     * @param contentRepository  Repository for Content entity operations
     * @param questionRepository Repository for Question entity operations
     */
    public ModuleService(ModuleRepository moduleRepository, TrainingService trainingService, ContentRepository contentRepository, QuestionRepository questionRepository, QuestionOptionRepository questionOptionRepository) {
        this.moduleRepository = moduleRepository;
        this.trainingService = trainingService;
        this.contentRepository = contentRepository;
        this.questionRepository = questionRepository;
        this.questionOptionRepository = questionOptionRepository;
    }

    /**
     * Retrieves all modules in the system.
     *
     * @return A list of all Module entities
     */
    public List<Module> getAllModules() {
        return moduleRepository.findAll();
    }

    /**
     * Creates and saves a new module.
     *
     * @param moduleDTO Data transfer object containing module information
     * @return The saved Module entity
     * @throws MissingArgumentException If training_id is missing from the DTO
     * @throws NotFoundException        If the training is not found
     */
    public Module save(ModuleDTO moduleDTO) {
        Module module = new Module();
        if (moduleDTO.trainingId() == 0) {
            throw new MissingArgumentException("training_id is missing");
        }
        Training training = trainingService.findById(moduleDTO.trainingId());
        module.setTraining(training);
        module.setTitle(moduleDTO.title());
        module.setDescription(moduleDTO.description());
        moduleRepository.save(module);
        return module;
    }

    /**
     * Finds a module by its ID.
     *
     * @param id The ID of the module to find
     * @return The found Module entity
     * @throws NotFoundException If no module with the given ID exists
     */
    public Module getModuleById(int id) {
        Module module = moduleRepository.findById(id).orElse(null);
        if (module == null) {
            throw new NotFoundException("Module with id " + id + " not found");
        }
        return module;
    }

    /**
     * Updates an existing module with new information.
     *
     * @param id        The ID of the module to update
     * @param moduleDTO Data transfer object containing updated module information
     * @throws NotFoundException If no module with the given ID exists
     */
    public Module updateModule(int id, ModuleDTO moduleDTO) {
        Module module = moduleRepository.findById(id).orElse(null);
        if (module == null) {
            throw new NotFoundException("Module with id " + id + " not found");
        }

        // Initialize maps if they are null
        if (module.getTitle() == null) {
            module.setTitle(new HashMap<>());
        }
        if (module.getDescription() == null) {
            module.setDescription(new HashMap<>());
        }

        // Update with the new values
        if (moduleDTO.title() != null) {
            module.getTitle().clear();
            module.getTitle().putAll(moduleDTO.title());
        }
        if (moduleDTO.description() != null) {
            module.getDescription().clear();
            module.getDescription().putAll(moduleDTO.description());
        }

        moduleRepository.save(module);
        return module;
    }

    /**
     * Deletes a whole module.
     *
     * @param id The ID of the module to delete.
     * @throws NotFoundException If no module with the given ID exists
     */
    public void deleteModule(int id) {
        Module module = this.getModuleById(id);
        if (module == null) {
            throw new NotFoundException("Module with id " + id + " not found");
        }
        moduleRepository.delete(module);
    }

    /**
     * Adds content to an existing module.
     *
     * @param id         The ID of the module to add content to
     * @param contentDTO Data transfer object containing content information
     * @return The saved Content entity
     * @throws NotFoundException If no module with the given ID exists
     */
    public Content addContent(int id, ContentDTO contentDTO) {
        Module module = moduleRepository.findById(id).orElse(null);
        if (module == null) {
            throw new NotFoundException("Module with id " + id + " not found");
        }
        Content content = new Content();
        content.setContentType(contentDTO.contentType());
        content.setReference(contentDTO.reference());
        content.setModule(module);
        contentRepository.save(content);
        return content;
    }

    /**
     * Updates content from an existing module.
     *
     * @param id         The ID of the module to add content to
     * @param idContent  The ID of the content
     * @param contentDTO Data transfer object containing content information
     * @return The saved Content entity
     * @throws NotFoundException If no module or no content with the given ID exists
     */
    public Content updateContent(int id, int idContent, ContentDTO contentDTO) {
        Module module = moduleRepository.findById(id).orElse(null);
        if (module == null) {
            throw new NotFoundException("Module with id " + id + " not found");
        }
        Content content = contentRepository.findById(idContent).orElse(null);
        if (content == null) {
            throw new NotFoundException("Content with id " + id + " not found");
        }
        content.setContentType(contentDTO.contentType());
        content.setReference(contentDTO.reference());
        contentRepository.save(content);
        return content;
    }

    /**
     * Deletes content from an existing module.
     *
     * @param id        The ID of the module to delete the content from
     * @param idContent The ID of the content that we need to delete
     */
    public void deleteContent(int id, int idContent) {
        Module module = this.getModuleById(id);
        if (module == null) {
            throw new NotFoundException("Module with id " + id + " not found");
        }
        Content content = contentRepository.findById(idContent).orElse(null);
        if (content == null) {
            throw new NotFoundException("Content with id " + id + " not found");
        }
        contentRepository.delete(content);
    }

    /**
     * Adds a question to an existing module.
     *
     * @param id          The ID of the module to add the question to
     * @param questionDTO Data transfer object containing question information
     * @return The saved Question entity
     * @throws NotFoundException If no module with the given ID exists
     */
    public Question addQuestion(int id, QuestionDTO questionDTO) {
        Module module = moduleRepository.findById(id).orElse(null);
        if (module == null) {
            throw new NotFoundException("Module with id " + id + " not found");
        }
        Question question = new Question();
        question.setModule(module);
        List<QuestionOption> questionOptions = questionDTO.questionOptions();
        for (QuestionOption questionOption : questionOptions) {
            questionOption.setQuestion(question);
        }
        question.setQuestionOptions(questionOptions);
        BeanUtils.copyProperties(questionDTO, question);
        questionRepository.save(question);
        return question;
    }

    /**
     * Retrieves all questions for a specific module.
     *
     * @param id The ID of the module to get questions for
     * @return A list of Question entities associated with the module
     * @throws NotFoundException If no module with the given ID exists or the module has no questions
     */
    public List<Question> getQuestionsByModuleId(int id) {
        Module module = moduleRepository.findById(id).orElse(null);
        if (module == null) {
            throw new NotFoundException("Module with id " + id + " not found");
        }
        if (module.getQuestions().isEmpty()) {
            throw new NotFoundException("Module with id " + id + " has no questions");
        }
        return module.getQuestions();
    }

    /**
     * Deletes all questions from a specific module.
     *
     * @param id The ID of the module to delete the questions for
     * @throws NotFoundException If no module with the given ID exists
     */
    public void deleteQuestions(int id) {
        Module module = this.getModuleById(id);
        if (module == null) {
            throw new NotFoundException("Module with id " + id + " not found");
        }
        for (Question question : module.getQuestions()) {
            questionOptionRepository.deleteAll(question.getQuestionOptions());
            questionRepository.delete(question);
        }
        module.getQuestions().clear();
        moduleRepository.save(module);
    }
}