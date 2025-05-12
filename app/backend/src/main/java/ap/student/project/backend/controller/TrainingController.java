package ap.student.project.backend.controller;

import ap.student.project.backend.dto.TrainingDTO;
import ap.student.project.backend.service.TrainingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class TrainingController {
    private final TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @GetMapping(value = "/trainings", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getModules() {
        return ResponseEntity.ok(this.trainingService.findAll());
    }

    @GetMapping(value = "/trainings/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getModuleById(@PathVariable Integer id) {
        return ResponseEntity.ok(this.trainingService.findById(id));
    }

    @PostMapping(value = "/trainings", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addModule(@RequestBody TrainingDTO trainingDTO) {
        this.trainingService.save(trainingDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(trainingDTO);
    }

    @PutMapping(value = "/trainings/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateModule(@PathVariable("id") int id, @RequestBody TrainingDTO trainingDTO) {
        this.trainingService.update(id, trainingDTO);
        return ResponseEntity.ok(trainingDTO);
    }
}
