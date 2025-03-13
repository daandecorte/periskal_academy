package ap.student.project.backend.controller;

import ap.student.project.backend.dto.ExamDTO;
import ap.student.project.backend.entity.Exam;
import ap.student.project.backend.exceptions.DuplicateException;
import ap.student.project.backend.service.ExamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class ExamController {
    private final ExamService examService;
    private final Logger logger = LoggerFactory.getLogger(ExamController.class);
    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @GetMapping(value = "/exams", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getExams() {
        return ResponseEntity.ok(this.examService.findAll());
    }

    @PostMapping(value = "/exams", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addModule(@RequestBody ExamDTO examDTO) {
        try {
            this.examService.save(examDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(examDTO);
        }
        catch(DuplicateException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
