package ap.student.project.backend.service;

import ap.student.project.backend.dao.TrainingRepository;
import ap.student.project.backend.dto.TrainingDTO;
import ap.student.project.backend.dto.VideoDTO;
import ap.student.project.backend.entity.Language;
import ap.student.project.backend.entity.Training;
import ap.student.project.backend.exceptions.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TrainingService {
    private final TrainingRepository trainingRepository;

    public TrainingService(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }
    public List<Training> getAllTrainings() {
        return trainingRepository.findAll();
    }
    public void save(TrainingDTO trainingDTO) {
        Training training = new Training();
        BeanUtils.copyProperties(trainingDTO, training);
        trainingRepository.save(training);
    }
    public Training getTrainingById(int id) {
        Training training = trainingRepository.findById(id).orElse(null);
        if (training == null) {
            throw new NotFoundException("Training with id " + id + " not found");
        }
        return training;
    }
    public void deleteTrainingById(int id) {
        if(trainingRepository.existsById(id)) {
            trainingRepository.deleteById(id);
        }
        else throw new NotFoundException("Training with id " + id + " not found");
    }
    public void updateTraining(int id, TrainingDTO trainingDTO) {
        Training training = trainingRepository.findById(id).orElse(null);
        if (training == null) {
            throw new NotFoundException("Training with id " + id + " not found");
        }
        BeanUtils.copyProperties(trainingDTO, training);
        trainingRepository.save(training);
    }
    public void addVideo(int id, VideoDTO videoDTO) {
        Training training = trainingRepository.findById(id).orElse(null);
        if (training == null) {
            throw new NotFoundException("Training with id " + id + " not found");
        }
        Map<Language, String> videoReference = training.getVideoReference();
        videoReference.put(videoDTO.language(), videoDTO.videoReference());
        training.setVideoReference(videoReference);
        trainingRepository.save(training);
    }
}
