package ap.student.project.backend.controller;

import ap.student.project.backend.dto.ExamAttemptDTO;
import ap.student.project.backend.service.ExamAttemptService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for handling exam attempt-related HTTP requests.
 * Manages operations for exam attempts including retrieval and creation.
 */
@CrossOrigin
@RestController
public class ExamAttemptController {
    private final ExamAttemptService examAttemptService;

    public ExamAttemptController(ExamAttemptService examAttemptService) {
        this.examAttemptService = examAttemptService;
    }

    /**
     * Retrieves all exam attempts from the system.
     * 
     * @return ResponseEntity containing a list of all exam attempts with HTTP status 200 (OK)
     */
    @GetMapping(value = "/exam_attempts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getExamAttempts() {
        return ResponseEntity.ok(this.examAttemptService.findAll());
    }

    /**
     * Creates a new exam attempt in the system.
     * 
     * @param examAttemptDTO The exam attempt data transfer object containing attempt information
     * @return ResponseEntity containing the created exam attempt with HTTP status 201 (CREATED)
     */
    @PostMapping(value = "/exam_attempts", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createExamAttempt(@RequestBody ExamAttemptDTO examAttemptDTO) {
        this.examAttemptService.save(examAttemptDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(examAttemptDTO);
    }
}
