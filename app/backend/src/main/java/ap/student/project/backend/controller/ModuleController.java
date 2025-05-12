package ap.student.project.backend.controller;

import ap.student.project.backend.dto.ContentDTO;
import ap.student.project.backend.dto.ModuleDTO;
import ap.student.project.backend.dto.QuestionDTO;
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
    public ResponseEntity getAllModules() {
        return ResponseEntity.ok(moduleService.getAllModules());
    }
    @PostMapping(value = "/modules", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addModule(@RequestBody ModuleDTO moduleDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(this.moduleService.save(moduleDTO));
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
    public ResponseEntity getModuleById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(moduleService.getModuleById(id));
        }
        catch (NotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PutMapping(value = "/modules/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateModule(@PathVariable Integer id, @RequestBody ModuleDTO moduleDTO) {
        try {
            this.moduleService.updateModule(id, moduleDTO);
            return ResponseEntity.ok(moduleDTO);
        }
        catch(NotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PostMapping(value = "/modules/{id}/content")
    public ResponseEntity addContent(@PathVariable Integer id, @RequestBody ContentDTO contentDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(moduleService.addContent(id, contentDTO));
        }
        catch(NotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PostMapping(value = "/modules/{id}/questions")
    public ResponseEntity addQuestion(@PathVariable("id") int id, @RequestBody QuestionDTO questionDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(moduleService.addQuestion(id, questionDTO));
        }
        catch(NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping(value = "/modules/{id}/questions")
    public ResponseEntity getQuestion(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(this.moduleService.getQuestionsByModuleId(id));
        }
        catch(NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
