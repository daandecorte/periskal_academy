package ap.student.project.backend.controller;

import ap.student.project.backend.dto.ModuleDTO;
import ap.student.project.backend.dto.VideoDTO;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import ap.student.project.backend.service.ModuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ModuleController {
    private final ModuleService moduleService;
    private final Logger logger = LoggerFactory.getLogger(ModuleController.class);

    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }
    @GetMapping(value = "/modules", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllTrainings() {
        return ResponseEntity.ok(moduleService.getAllTrainings());
    }
    @PostMapping(value = "/modules", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addTraining(@RequestBody ModuleDTO moduleDTO) {
        try {
            this.moduleService.save(moduleDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(moduleDTO);
        }
        catch (NotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (MissingArgumentException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @GetMapping(value = "/modules/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTrainingById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(moduleService.getTrainingById(id));
        }
        catch (NotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PutMapping(value = "/modules/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateTraining(@PathVariable Integer id, @RequestBody ModuleDTO moduleDTO) {
        try {
            this.moduleService.updateTraining(id, moduleDTO);
            return ResponseEntity.ok(moduleDTO);
        }
        catch(NotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @DeleteMapping(value = "/modules/{id}")
    public ResponseEntity deleteTraining(@PathVariable Integer id) {
        try {
            moduleService.deleteTrainingById(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        catch (NotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PostMapping(value = "/modules/{id}/video")
    public ResponseEntity addVideo(@PathVariable Integer id, @RequestBody VideoDTO videoDTO) {
        try {
            moduleService.addVideo(id, videoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(videoDTO);
        }
        catch(NotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
