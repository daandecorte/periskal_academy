package ap.student.project.backend.controller;

import ap.student.project.backend.dto.ModuleDTO;
import ap.student.project.backend.exceptions.DuplicateException;
import ap.student.project.backend.exceptions.NotFoundException;
import ap.student.project.backend.service.ModuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class ModuleController {
    private final ModuleService moduleService;
    private final Logger logger = LoggerFactory.getLogger(ModuleController.class);

    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @GetMapping(value = "/modules", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getModules() {
        return ResponseEntity.ok(this.moduleService.findAll());
    }

    @GetMapping(value = "/modules/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getModuleById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(this.moduleService.findById(id));
        } catch (NotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping(value = "/modules", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addModule(@RequestBody ModuleDTO moduleDTO) {
        try {
            this.moduleService.save(moduleDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(moduleDTO);
        } catch (DuplicateException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PutMapping(value = "/modules/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateModule(@PathVariable("id") int id, @RequestBody ModuleDTO moduleDTO) {
        try {
            this.moduleService.update(id, moduleDTO);
            return ResponseEntity.ok(moduleDTO);
        } catch (NotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/modules/{id}")
    public ResponseEntity deleteModule(@PathVariable("id") int id) {
        moduleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
