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

    public void save(TrainingProgressDTO trainingProgressDTO) {
        TrainingProgress trainingProgress = new TrainingProgress();
        if(trainingProgressDTO.userTrainingId()==0) {
            throw new MissingArgumentException("user_training_id is missing");
        }
        UserTraining userTraining = userTrainingService.findById(trainingProgressDTO.userTrainingId());
        trainingProgress.setUserTraining(userTraining);
        BeanUtils.copyProperties(trainingProgressDTO, trainingProgress);
        trainingProgressRepository.save(trainingProgress);
    }
    public void update(int id, TrainingProgressDTO trainingProgressDTO) throws NotFoundException {
        TrainingProgress trainingProgress = trainingProgressRepository.findById(id).orElse(null);
        if (trainingProgress == null) {
            throw new NotFoundException("Training progress with id " + id + " not found");
        }
        BeanUtils.copyProperties(trainingProgressDTO, trainingProgress);
        trainingProgressRepository.save(trainingProgress);
    }

    public List<TrainingProgress> findAll() {
        return trainingProgressRepository.findAll();
    }
}
