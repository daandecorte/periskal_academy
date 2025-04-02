package ap.student.project.backend.controller;

import ap.student.project.backend.dto.ModuleProgressDTO;
import ap.student.project.backend.exceptions.NotFoundException;
import ap.student.project.backend.service.ModuleProgressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class ModuleProgressController {
    private final ModuleProgressService moduleProgressService;

    public ModuleProgressController(ModuleProgressService moduleProgressService) {
        this.moduleProgressService = moduleProgressService;
    }

    @GetMapping(value = "/module_progress", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getModuleProgress() {
        return ResponseEntity.ok(this.moduleProgressService.findAll());
    }

    @PostMapping(value = "/module_progress", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createModuleProgress(@RequestBody ModuleProgressDTO moduleProgressDTO) {
        try {
            this.moduleProgressService.save(moduleProgressDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(moduleProgressDTO);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PutMapping(value="module_progress/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateModuleProgress(@PathVariable("id") int id, @RequestBody ModuleProgressDTO moduleProgressDTO) {
        try {
            this.moduleProgressService.update(id, moduleProgressDTO);
            return ResponseEntity.ok(moduleProgressDTO);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
