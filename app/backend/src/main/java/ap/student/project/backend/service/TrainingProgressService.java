package ap.student.project.backend.service;

import ap.student.project.backend.dao.TrainingProgressRepository;
import ap.student.project.backend.dto.TrainingProgressDTO;
import ap.student.project.backend.entity.TrainingProgress;
import ap.student.project.backend.entity.UserTraining;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingProgressService {
    private final TrainingProgressRepository trainingProgressRepository;
    private final UserTrainingService userTrainingService;

    public TrainingProgressService(TrainingProgressRepository trainingProgressRepository, UserTrainingService userTrainingService) {
        this.trainingProgressRepository = trainingProgressRepository;
        this.userTrainingService = userTrainingService;
    }

    public TrainingProgress save(TrainingProgressDTO trainingProgressDTO) {
        if(trainingProgressDTO.userTrainingId()==0) {
            throw new MissingArgumentException("user_training_id is missing");
        }
        UserTraining userTraining = userTrainingService.findById(trainingProgressDTO.userTrainingId());

        // Check if this UserTraining already has a TrainingProgress
        if (userTraining.getTrainingProgress() != null) {
            throw new IllegalStateException("UserTraining with id " + userTraining.getId() + 
                " already has a TrainingProgress with id " + userTraining.getTrainingProgress().getId());
        }
        
        TrainingProgress trainingProgress = new TrainingProgress();
        trainingProgress.setUserTraining(userTraining);
        trainingProgress.setStartDateTime(trainingProgressDTO.startDateTime());
        trainingProgress.setLastTimeAccessed(trainingProgressDTO.lastTimeAccessed());
        trainingProgress.setStatus(trainingProgressDTO.status());
        
        TrainingProgress savedProgress = trainingProgressRepository.save(trainingProgress);
        
        // Establish bidirectional relationship
        userTraining.setTrainingProgress(savedProgress);
        
        return savedProgress;
    }

    public TrainingProgress update(int id, TrainingProgressDTO trainingProgressDTO) throws NotFoundException {
        TrainingProgress trainingProgress = trainingProgressRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Training progress with id " + id + " not found"));
        
        // Only update fields that don't involve relationships
        trainingProgress.setStartDateTime(trainingProgressDTO.startDateTime());
        trainingProgress.setLastTimeAccessed(trainingProgressDTO.lastTimeAccessed());
        trainingProgress.setStatus(trainingProgressDTO.status());
        
        return trainingProgressRepository.save(trainingProgress);
    }

    public List<TrainingProgress> findAll() {
        return trainingProgressRepository.findAll();
    }

    public TrainingProgress findById(int id) throws NotFoundException {
        return trainingProgressRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Training progress with id " + id + " not found"));
    }

    public TrainingProgress findByUserTrainingId(int userTrainingId) {
        return trainingProgressRepository.findByUserTrainingId(userTrainingId);
    }
}
