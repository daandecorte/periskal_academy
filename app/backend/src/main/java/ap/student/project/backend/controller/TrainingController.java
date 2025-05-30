package ap.student.project.backend.controller;

import ap.student.project.backend.dto.TrainingDTO;
import ap.student.project.backend.service.TrainingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for handling training-related HTTP requests.
 * Manages operations for trainings including creation, retrieval, and updates.
 */
@CrossOrigin
@RestController
public class TrainingController {
    private final TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    /**
     * Retrieves all trainings from the system.
     *
     * @return ResponseEntity containing a list of all trainings with HTTP status 200 (OK)
     */
    @GetMapping(value = "/trainings", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getModules() {
        return ResponseEntity.ok(this.trainingService.findAll());
    }

    /**
     * Retrieves a specific training by its ID.
     *
     * @param id The ID of the training to retrieve
     * @return ResponseEntity containing the training with HTTP status 200 (OK)
     */
    @GetMapping(value = "/trainings/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getModuleById(@PathVariable Integer id) {
        return ResponseEntity.ok(this.trainingService.findById(id));
    }

    /**
     * Creates a new training in the system.
     *
     * @param trainingDTO The training data transfer object containing training information
     * @return ResponseEntity containing the created training with HTTP status 201 (CREATED)
     */
    @PostMapping(value = "/trainings", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addModule(@RequestBody TrainingDTO trainingDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.trainingService.save(trainingDTO));
    }

    /**
     * Updates a specific training with new information.
     *
     * @param id          The ID of the training to update
     * @param trainingDTO The training data transfer object containing updated training information
     * @return ResponseEntity containing the updated training with HTTP status 200 (OK)
     */
    @PutMapping(value = "/trainings/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateModule(@PathVariable("id") int id, @RequestBody TrainingDTO trainingDTO) {
        this.trainingService.update(id, trainingDTO);
        return ResponseEntity.ok(trainingDTO);
    }
}
