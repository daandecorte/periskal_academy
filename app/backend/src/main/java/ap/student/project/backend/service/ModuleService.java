package ap.student.project.backend.service;

import ap.student.project.backend.dao.ModuleRepository;
import ap.student.project.backend.dto.ModuleDTO;
import ap.student.project.backend.dto.VideoDTO;
import ap.student.project.backend.entity.Language;
import ap.student.project.backend.entity.Module;
import ap.student.project.backend.entity.Training;
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

    public ModuleService(ModuleRepository moduleRepository, TrainingService trainingService) {
        this.moduleRepository = moduleRepository;
        this.trainingService = trainingService;
    }
    public List<Module> getAllTrainings() {
        return moduleRepository.findAll();
    }
    public void save(ModuleDTO moduleDTO) {
        Module module = new Module();
        if(moduleDTO.trainingId()==0) {
            throw new MissingArgumentException("training_id is missing");
        }
        Training training = trainingService.findById(moduleDTO.trainingId());
        module.setTraining(training);
        BeanUtils.copyProperties(moduleDTO, module);
        moduleRepository.save(module);
    }
    public Module getTrainingById(int id) {
        Module module = moduleRepository.findById(id).orElse(null);
        if (module == null) {
            throw new NotFoundException("Module with id " + id + " not found");
        }
        return module;
    }
    public void deleteTrainingById(int id) {
        if(moduleRepository.existsById(id)) {
            moduleRepository.deleteById(id);
        }
        else throw new NotFoundException("Module with id " + id + " not found");
    }
    public void updateTraining(int id, ModuleDTO moduleDTO) {
        Module module = moduleRepository.findById(id).orElse(null);
        if (module == null) {
            throw new NotFoundException("Module with id " + id + " not found");
        }
        BeanUtils.copyProperties(moduleDTO, module);
        moduleRepository.save(module);
    }
    public void addVideo(int id, VideoDTO videoDTO) {
        Module module = moduleRepository.findById(id).orElse(null);
        if (module == null) {
            throw new NotFoundException("Module with id " + id + " not found");
        }
        Map<Language, String> videoReference = module.getVideoReference();
        videoReference.put(videoDTO.language(), videoDTO.videoReference());
        module.setVideoReference(videoReference);
        moduleRepository.save(module);
    }
}
