package ap.student.project.backend.controller;

import ap.student.project.backend.dto.ContentDTO;
import ap.student.project.backend.dto.ModuleDTO;
import ap.student.project.backend.dto.QuestionDTO;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import ap.student.project.backend.service.ModuleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ModuleController {
    private final ModuleService moduleService;

    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @GetMapping(value = "/modules", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllModules() {
        return ResponseEntity.ok(moduleService.getAllModules());
    }

    @PostMapping(value = "/modules", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addModule(@RequestBody ModuleDTO moduleDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.moduleService.save(moduleDTO));
    }

    @GetMapping(value = "/modules/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getModuleById(@PathVariable Integer id) {
        return ResponseEntity.ok(moduleService.getModuleById(id));
    }

    @PutMapping(value = "/modules/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateModule(@PathVariable Integer id, @RequestBody ModuleDTO moduleDTO) {
        this.moduleService.updateModule(id, moduleDTO);
        return ResponseEntity.ok(moduleDTO);
    }

    @PostMapping(value = "/modules/{id}/content")
    public ResponseEntity addVideo(@PathVariable Integer id, @RequestBody ContentDTO contentDTO) {
        
        return ResponseEntity.status(HttpStatus.CREATED).body(moduleService.addContent(id, contentDTO));
    }

    @PostMapping(value = "/modules/{id}/questions")
    public ResponseEntity addQuestion(@PathVariable("id") int id, @RequestBody QuestionDTO questionDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(moduleService.addQuestion(id, questionDTO));
    }

    @GetMapping(value = "/modules/{id}/questions")
    public ResponseEntity getQuestion(@PathVariable("id") int id) {
        return ResponseEntity.ok(this.moduleService.getQuestionsByModuleId(id));
    }
}
