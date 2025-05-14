package ap.student.project.backend.controller;

import ap.student.project.backend.dto.TrainingProgressDTO;
import ap.student.project.backend.service.TrainingProgressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for handling training progress-related HTTP requests.
 * Manages operations for tracking user progress through trainings including
 * retrieval, creation, and updates.
 */
@CrossOrigin
@RestController
public class TrainingProgressController {
    private final TrainingProgressService trainingProgressService;

    public TrainingProgressController(TrainingProgressService trainingProgressService) {
        this.trainingProgressService = trainingProgressService;
    }

/**
     * Retrieves all training progress records from the system.
     * 
     * @return ResponseEntity containing a list of all training progress records with HTTP status 200 (OK)
     */
    @GetMapping(value = "/training_progress", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getModuleProgress() {
        return ResponseEntity.ok(this.trainingProgressService.findAll());
    }

    /**
     * Creates a new training progress record in the system.
     * 
     * @param trainingProgressDTO The training progress data transfer object containing progress information
     * @return ResponseEntity containing the created training progress record with HTTP status 201 (CREATED)
     */
    @PostMapping(value = "/training_progress", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createModuleProgress(@RequestBody TrainingProgressDTO trainingProgressDTO) {
        this.trainingProgressService.save(trainingProgressDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(trainingProgressDTO);
    }

    /**
     * Updates a specific training progress record with new information.
     * 
     * @param id The ID of the training progress record to update
     * @param trainingProgressDTO The training progress data transfer object containing updated progress information
     * @return ResponseEntity containing the updated training progress record with HTTP status 200 (OK)
     */
    @PutMapping(value = "training_progress/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateModuleProgress(@PathVariable("id") int id, @RequestBody TrainingProgressDTO trainingProgressDTO) {
        this.trainingProgressService.update(id, trainingProgressDTO);
        return ResponseEntity.ok(trainingProgressDTO);
    }
}
