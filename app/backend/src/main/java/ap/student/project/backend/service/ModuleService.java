package ap.student.project.backend.service;

import ap.student.project.backend.dao.ContentRepository;
import ap.student.project.backend.dao.ModuleRepository;
import ap.student.project.backend.dao.QuestionRepository;
import ap.student.project.backend.dto.ContentDTO;
import ap.student.project.backend.dto.ModuleDTO;
import ap.student.project.backend.dto.QuestionDTO;
import ap.student.project.backend.entity.*;
import ap.student.project.backend.entity.Module;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ModuleService {
    private final ModuleRepository moduleRepository;
    private final TrainingService trainingService;
    private final ContentRepository contentRepository;
    private final QuestionRepository questionRepository;

    public ModuleService(ModuleRepository moduleRepository, TrainingService trainingService, ContentRepository contentRepository, QuestionRepository questionRepository) {
        this.moduleRepository = moduleRepository;
        this.trainingService = trainingService;
        this.contentRepository = contentRepository;
        this.questionRepository = questionRepository;
    }
    public List<Module> getAllModules() {
        return moduleRepository.findAll();
    }
    public Module save(ModuleDTO moduleDTO) {
        Module module = new Module();
        if(moduleDTO.trainingId()==0) {
            throw new MissingArgumentException("training_id is missing");
        }
        Training training = trainingService.findById(moduleDTO.trainingId());
        module.setTraining(training);
        module.setTitle(moduleDTO.title());
        module.setDescription(moduleDTO.description());
        moduleRepository.save(module);
        return module;
    }
    public Module getModuleById(int id) {
        Module module = moduleRepository.findById(id).orElse(null);
        if (module == null) {
            throw new NotFoundException("Module with id " + id + " not found");
        }
        return module;
    }
    public void updateModule(int id, ModuleDTO moduleDTO) {
        Module module = moduleRepository.findById(id).orElse(null);
        if (module == null) {
            throw new NotFoundException("Module with id " + id + " not found");
        }
        BeanUtils.copyProperties(moduleDTO, module);
        moduleRepository.save(module);
    }
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
    public Question addQuestion(int id, QuestionDTO questionDTO) {
        Module module = moduleRepository.findById(id).orElse(null);
        if (module == null) {
            throw new NotFoundException("Module with id " + id + " not found");
        }
        Question question = new Question();
        question.setModule(module);
        List<QuestionOption> questionOptions = questionDTO.questionOptions();
        for(QuestionOption questionOption : questionOptions) {
            questionOption.setQuestion(question);
        }
        question.setQuestionOptions(questionOptions);
        BeanUtils.copyProperties(questionDTO, question);
        questionRepository.save(question);
        return question;
    }
    public List<Question> getQuestionsByModuleId(int id) {
        Module module = moduleRepository.findById(id).orElse(null);
        if (module == null) {
            throw new NotFoundException("Module with id " + id + " not found");
        }
        if(module.getQuestions().isEmpty()) {
            throw new NotFoundException("Module with id " + id + " has no questions");
        }
        return module.getQuestions();
    }
}
