package ap.student.project.backend.controller;

import ap.student.project.backend.dto.TrainingProgressDTO;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import ap.student.project.backend.service.TrainingProgressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class TrainingProgressController {
    private final TrainingProgressService trainingProgressService;

    public TrainingProgressController(TrainingProgressService trainingProgressService) {
        this.trainingProgressService = trainingProgressService;
    }

    @GetMapping(value = "/training_progress", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getModuleProgress() {
        return ResponseEntity.ok(this.trainingProgressService.findAll());
    }

    @PostMapping(value = "/training_progress", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createModuleProgress(@RequestBody TrainingProgressDTO trainingProgressDTO) {
        try {
            this.trainingProgressService.save(trainingProgressDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(trainingProgressDTO);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (MissingArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PutMapping(value="training_progress/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateModuleProgress(@PathVariable("id") int id, @RequestBody TrainingProgressDTO trainingProgressDTO) {
        try {
            this.trainingProgressService.update(id, trainingProgressDTO);
            return ResponseEntity.ok(trainingProgressDTO);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
