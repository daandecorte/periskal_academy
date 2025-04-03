package ap.student.project.backend.controller;

import ap.student.project.backend.dto.ExamAttemptDTO;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import ap.student.project.backend.service.ExamAttemptService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class ExamAttemptController {
    private final ExamAttemptService examAttemptService;

    public ExamAttemptController(ExamAttemptService examAttemptService) {
        this.examAttemptService = examAttemptService;
    }

    @GetMapping(value = "/exam_attempts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getExamAttempts() {
        return ResponseEntity.ok(this.examAttemptService.findAll());
    }

    @PostMapping(value = "/exam_attempts", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createExamAttempt(@RequestBody ExamAttemptDTO examAttemptDTO) {
        try {
            this.examAttemptService.save(examAttemptDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(examAttemptDTO);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (MissingArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
