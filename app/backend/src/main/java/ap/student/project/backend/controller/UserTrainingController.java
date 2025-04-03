package ap.student.project.backend.controller;

import ap.student.project.backend.dto.UserTrainingDTO;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import ap.student.project.backend.service.UserTrainingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class UserTrainingController {
    private final UserTrainingService userTrainingService;

    public UserTrainingController(UserTrainingService userTrainingService) {
        this.userTrainingService = userTrainingService;
    }

    @GetMapping(value = "/user_trainings", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUserModules() {
        return ResponseEntity.ok(this.userTrainingService.findAll());
    }

    @PostMapping(value = "/user_trainings", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createUserModule(@RequestBody UserTrainingDTO userTrainingDTO) {
        try {
            this.userTrainingService.save(userTrainingDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(userTrainingDTO);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (MissingArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
