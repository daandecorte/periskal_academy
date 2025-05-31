package ap.student.project.backend.controller;

import ap.student.project.backend.dto.UserTrainingDTO;
import ap.student.project.backend.service.UserTrainingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for handling user training-related HTTP requests.
 * Manages operations for user trainings including creation and retrieval.
 */
@CrossOrigin
@RestController
public class UserTrainingController {
    private final UserTrainingService userTrainingService;

    public UserTrainingController(UserTrainingService userTrainingService) {
        this.userTrainingService = userTrainingService;
    }

    /**
     * Retrieves all user training records from the system.
     *
     * @return ResponseEntity containing a list of all user training records with HTTP status 200 (OK)
     */
    @GetMapping(value = "/user_trainings", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUserModules() {
        return ResponseEntity.ok(this.userTrainingService.findAll());
    }

    /**
     * Creates a new user training record in the system.
     *
     * @param userTrainingDTO The user training data transfer object containing training association information
     * @return ResponseEntity containing the created user training record with HTTP status 201 (CREATED)
     */
    @PostMapping(value = "/user_trainings", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createUserModule(@RequestBody UserTrainingDTO userTrainingDTO) {
        this.userTrainingService.save(userTrainingDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userTrainingDTO);
    }

    /**
     * Finds a user training with a training id and a user id.
     *
     * @param trainingId The training id
     * @param userId     The user id
     * @return ResponseEntity containing the found user training record with HTTP status 200 (OK)
     */
    @GetMapping(value = "/user_trainings/training/{training_id}/user/{user_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity findUserTrainingByTrainingIdAndUserId(@PathVariable("training_id") int trainingId, @PathVariable("user_id") int userId) {
        return ResponseEntity.ok(this.userTrainingService.findByTrainingIdAndUserId(trainingId, userId));
    }

    /**
     * Updates an existing user training record.
     *
     * @param id              the ID of the user training record to update
     * @param userTrainingDTO the updated training data for the user
     * @return ResponseEntity containing the updated UserTrainingDTO
     */
    @PutMapping(value = "/user_trainings/{id}")
    public ResponseEntity updateUserTraining(@PathVariable("id") int id, @RequestBody UserTrainingDTO userTrainingDTO) {
        this.userTrainingService.update(id, userTrainingDTO);
        return ResponseEntity.ok(userTrainingDTO);
    }
}
