package ap.student.project.backend.controller;

import ap.student.project.backend.dto.ExamAttemptDTO;
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
        this.examAttemptService.save(examAttemptDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(examAttemptDTO);
    }
}
