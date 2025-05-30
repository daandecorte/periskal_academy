package ap.student.project.backend.controller;

import ap.student.project.backend.dto.ContentDTO;
import ap.student.project.backend.dto.ModuleDTO;
import ap.student.project.backend.dto.QuestionDTO;
import ap.student.project.backend.service.ModuleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for handling module-related HTTP requests.
 * Manages operations for modules including creation, retrieval, updates,
 * and managing module content and questions.
 */
@Controller
public class ModuleController {
    private final ModuleService moduleService;

    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    /**
     * Retrieves all modules from the system.
     *
     * @return ResponseEntity containing a list of all modules with HTTP status 200 (OK)
     */
    @GetMapping(value = "/modules", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllModules() {
        return ResponseEntity.ok(moduleService.getAllModules());
    }

    /**
     * Creates a new module in the system.
     *
     * @param moduleDTO The module data transfer object containing module information
     * @return ResponseEntity containing the created module with HTTP status 201 (CREATED)
     */
    @PostMapping(value = "/modules", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addModule(@RequestBody ModuleDTO moduleDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.moduleService.save(moduleDTO));
    }

    /**
     * Retrieves a specific module by its ID.
     *
     * @param id The ID of the module to retrieve
     * @return ResponseEntity containing the module with HTTP status 200 (OK)
     */
    @GetMapping(value = "/modules/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getModuleById(@PathVariable Integer id) {
        return ResponseEntity.ok(moduleService.getModuleById(id));
    }

    /**
     * Updates a specific module with new information.
     *
     * @param id        The ID of the module to update
     * @param moduleDTO The module data transfer object containing updated module information
     * @return ResponseEntity containing the updated module with HTTP status 200 (OK)
     */
    @PutMapping(value = "/modules/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateModule(@PathVariable Integer id, @RequestBody ModuleDTO moduleDTO) {
        return ResponseEntity.ok(this.moduleService.updateModule(id, moduleDTO));
    }

    /**
     * Deletes a specific module.
     *
     * @param id The ID of the module to delete
     * @return ResponseEntity containing nothing with HTTP status 200 (OK)
     */
    @DeleteMapping(value = "/modules/{id}")
    public ResponseEntity deleteModule(@PathVariable Integer id) {
        this.moduleService.deleteModule(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Adds content to a specific module.
     *
     * @param id         The ID of the module to add content to
     * @param contentDTO The content data transfer object containing content information
     * @return ResponseEntity containing the added content with HTTP status 201 (CREATED)
     */
    @PostMapping(value = "/modules/{id}/content")
    public ResponseEntity addContent(@PathVariable Integer id, @RequestBody ContentDTO contentDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(moduleService.addContent(id, contentDTO));
    }

    /**
     * Updates a specific content to a specific module.
     *
     * @param id         The ID of the module to add content to
     * @param idContent  The ID of the content
     * @param contentDTO The content data transfer object containing content information
     * @return ResponseEntity containing the added content with HTTP status 201 (CREATED)
     */
    @PutMapping(value = "/modules/{id}/content/{idContent}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateContent(@PathVariable Integer id, @PathVariable Integer idContent, @RequestBody ContentDTO contentDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(moduleService.updateContent(id, idContent, contentDTO));
    }

    /**
     * Deletes the content of a specific module.
     *
     * @param id        The ID of the module to delete the content from
     * @param idContent The ID of the content
     * @return
     */
    @DeleteMapping(value = "/modules/{id}/content/{idContent}")
    public ResponseEntity deleteContent(@PathVariable Integer id, @PathVariable Integer idContent) {
        moduleService.deleteContent(id, idContent);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Adds a question to a specific module.
     *
     * @param id          The ID of the module to add a question to
     * @param questionDTO The question data transfer object containing question information
     * @return ResponseEntity containing the added question with HTTP status 201 (CREATED)
     */
    @PostMapping(value = "/modules/{id}/questions")
    public ResponseEntity addQuestion(@PathVariable("id") int id, @RequestBody QuestionDTO questionDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(moduleService.addQuestion(id, questionDTO));
    }

    /**
     * Retrieves all questions for a specific module.
     *
     * @param id The ID of the module to retrieve questions for
     * @return ResponseEntity containing a list of module questions with HTTP status 200 (OK)
     */
    @GetMapping(value = "/modules/{id}/questions")
    public ResponseEntity getQuestion(@PathVariable("id") int id) {
        return ResponseEntity.ok(this.moduleService.getQuestionsByModuleId(id));
    }

    /**
     * Deletes all questions for a specific module.
     *
     * @param id The ID of the module to delete questions for
     * @return ResponseEntity containing nothing with HTTP status 200 (OK)
     */
    @DeleteMapping(value = "/modules/{id}/questions")
    public ResponseEntity deleteQuestions(@PathVariable("id") int id) {
        this.moduleService.deleteQuestions(id);
        return ResponseEntity.ok().build();
    }
}
