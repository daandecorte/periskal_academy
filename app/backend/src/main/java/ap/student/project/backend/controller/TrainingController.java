package ap.student.project.backend.controller;

import ap.student.project.backend.dto.TrainingDTO;
import ap.student.project.backend.entity.Training;
import ap.student.project.backend.exceptions.DuplicateException;
import ap.student.project.backend.exceptions.NotFoundException;
import ap.student.project.backend.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class TrainingController {
    private final TrainingService trainingService;
    private final Logger logger = LoggerFactory.getLogger(TrainingController.class);

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }
    @GetMapping(value = "/trainings", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllTrainings() {
        return ResponseEntity.ok(trainingService.getAllTrainings());
    }
    @PostMapping(value = "/trainings", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addTraining(@RequestBody TrainingDTO trainingDTO) {
        try {
            this.trainingService.save(trainingDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(trainingDTO);
        }
        catch (DuplicateException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    @GetMapping(value = "/trainings/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTrainingById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(trainingService.getTrainingById(id));
        }
        catch (NotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PutMapping(value = "/trainings/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateTraining(@PathVariable Integer id, @RequestBody TrainingDTO trainingDTO) {
        try {
            this.trainingService.updateTraining(id, trainingDTO);
            return ResponseEntity.ok(trainingDTO);
        }
        catch(NotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @DeleteMapping(value = "/trainings/{id}")
    public ResponseEntity deleteTraining(@PathVariable Integer id) {
        try {
            trainingService.deleteTrainingById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        catch (NotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
