package ap.student.project.backend.controller;

import ap.student.project.backend.dto.ExamDTO;
import ap.student.project.backend.dto.QuestionDTO;
import ap.student.project.backend.exceptions.DuplicateException;
import ap.student.project.backend.exceptions.ListFullException;
import ap.student.project.backend.exceptions.NotFoundException;
import ap.student.project.backend.service.ExamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(value = "/exams", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addExam(@RequestBody ExamDTO examDTO) {
        try {
            this.examService.save(examDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(examDTO);
        } catch (DuplicateException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    @GetMapping(value = "/exams/{id}/questions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getQuestions(@PathVariable int id) {
        return ResponseEntity.ok(this.examService.findAllQuestionsByExamId(id));
    }

    @PostMapping(value="/exams/{id}/questions", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addQuestion(@PathVariable int id, @RequestBody QuestionDTO questionDTO) {
        try {
            this.examService.addQuestion(id, questionDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(questionDTO);
        }
        catch(NotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch(ListFullException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
