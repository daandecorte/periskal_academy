package ap.student.project.backend.service;

import ap.student.project.backend.dao.TrainingRepository;
import ap.student.project.backend.dto.TrainingDTO;
import ap.student.project.backend.entity.Training;
import ap.student.project.backend.exceptions.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class that manages operations related to Training entities.
 * This service handles creating, retrieving, and updating Training records.
 */
@Service
public class TrainingService {
    private final TrainingRepository trainingRepository;

    /**
     * Constructs a new TrainingService with the necessary dependencies.
     *
     * @param trainingRepository The repository used for Training entity persistence operations
     */
    public TrainingService(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    /**
     * Creates and saves a new Training entity from the provided DTO.
     *
     * @param trainingDTO The data transfer object containing the Training information
     * @return The newly created and saved Training entity
     */
    public Training save(TrainingDTO trainingDTO) {
        Training training = new Training();
        BeanUtils.copyProperties(trainingDTO, training);
        return trainingRepository.save(training);
    }

    /**
     * Finds a Training entity by its ID.
     *
     * @param id The ID of the Training to find
     * @return The found Training entity
     * @throws NotFoundException If no Training with the given ID exists
     */
    public Training findById(int id) {
        Training training = trainingRepository.findById(id).orElse(null);
        if(training == null) {
            throw new NotFoundException("Training with id " + id + " not found");
        }
        return training;
    }

    /**
     * Updates an existing Training entity with new information from the provided DTO.
     *
     * @param id The ID of the Training to update
     * @param trainingDTO The data transfer object containing the updated Training information
     * @throws NotFoundException If no Training with the given ID exists
     */
    public void update(int id, TrainingDTO trainingDTO) {
        Training training = trainingRepository.findById(id).orElse(null);
        if(training == null) {
            throw new NotFoundException("Training with id " + id + " not found");
        }
        BeanUtils.copyProperties(trainingDTO, training);
        trainingRepository.save(training);
    }

    /**
     * Retrieves all Training entities from the database.
     *
     * @return A list containing all Training entities
     */
    public List<Training> findAll() {
        return trainingRepository.findAll();
    }
}
