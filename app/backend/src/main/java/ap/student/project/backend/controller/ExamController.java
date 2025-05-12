package ap.student.project.backend.controller;

import ap.student.project.backend.dto.ExamDTO;
import ap.student.project.backend.dto.ExamResultDTO;
import ap.student.project.backend.dto.ExamSubmissionDTO;
import ap.student.project.backend.dto.QuestionDTO;
import ap.student.project.backend.exceptions.DuplicateException;
import ap.student.project.backend.exceptions.ListFullException;
import ap.student.project.backend.exceptions.MissingArgumentException;
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

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @GetMapping(value = "/exams", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getExams() {
        return ResponseEntity.ok(this.examService.findAll());
    }

    @PostMapping(value = "/exams", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addExam(@RequestBody ExamDTO examDTO) {
        this.examService.save(examDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(examDTO);
    }

    @GetMapping(value = "/exams/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getExamById(@PathVariable int id) {
        this.examService.findById(id);
        return ResponseEntity.ok(this.examService.findById(id));
    }

    @PutMapping(value = "/exams/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateExam(@PathVariable int id, @RequestBody ExamDTO examDTO) {
        this.examService.update(id, examDTO);
        return ResponseEntity.ok(examDTO);
    }

    @GetMapping(value = "/exams/{id}/questions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getQuestions(@PathVariable int id) {
        return ResponseEntity.ok(this.examService.findAllQuestionsByExamId(id));
    }

    @PostMapping(value="/exams/{id}/questions", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addQuestion(@PathVariable int id, @RequestBody QuestionDTO questionDTO) {
        this.examService.addQuestion(id, questionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(questionDTO);
    }

    @PostMapping(value = "/exams/submit", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity submitExam(@RequestBody ExamSubmissionDTO submissionDTO) {
        ExamResultDTO result = this.examService.evaluateExam(submissionDTO);
        return ResponseEntity.ok(result);
    }
}
