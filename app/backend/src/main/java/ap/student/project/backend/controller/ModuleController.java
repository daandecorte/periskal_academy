package ap.student.project.backend.controller;

import ap.student.project.backend.dto.ModuleDTO;
import ap.student.project.backend.dto.QuestionDTO;
import ap.student.project.backend.dto.VideoDTO;
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
        this.moduleService.save(moduleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(moduleDTO);
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

    @PostMapping(value = "/modules/{id}/video")
    public ResponseEntity addVideo(@PathVariable Integer id, @RequestBody VideoDTO videoDTO) {
        moduleService.addVideo(id, videoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(videoDTO);
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
