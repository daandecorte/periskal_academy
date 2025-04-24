package ap.student.project.backend.service;

import ap.student.project.backend.dao.TrainingRepository;
import ap.student.project.backend.dto.TrainingDTO;
import ap.student.project.backend.entity.Training;
import ap.student.project.backend.exceptions.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainingService {
    private final TrainingRepository trainingRepository;

    public TrainingService(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    public Training save(TrainingDTO trainingDTO) {
        Training training = new Training();
        BeanUtils.copyProperties(trainingDTO, training);
        return trainingRepository.save(training);
    }
    public Training findById(int id) {
        Training training = trainingRepository.findById(id).orElse(null);
        if(training == null) {
            throw new NotFoundException("Training with id " + id + " not found");
        }
        return training;
    }
    public void update(int id, TrainingDTO trainingDTO) {
        Training training = trainingRepository.findById(id).orElse(null);
        if(training == null) {
            throw new NotFoundException("Training with id " + id + " not found");
        }
        BeanUtils.copyProperties(trainingDTO, training);
        trainingRepository.save(training);
    }
    public List<Training> findAll() {
        return trainingRepository.findAll();
    }
}
