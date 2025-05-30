package ap.student.project.backend.controller;

import ap.student.project.backend.dto.ExamDTO;
import ap.student.project.backend.dto.ExamResultDTO;
import ap.student.project.backend.dto.ExamSubmissionDTO;
import ap.student.project.backend.dto.QuestionDTO;
import ap.student.project.backend.service.ExamService;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for handling exam-related HTTP requests.
 * Manages operations for exams including creation, retrieval, updates,
 * managing exam questions, and evaluating exam submissions.
 */
@CrossOrigin
@RestController
public class ExamController {
    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    /**
     * Retrieves all exams from the system.
     * 
     * @return ResponseEntity containing a list of all exams with HTTP status 200 (OK)
     */
    @GetMapping(value = "/exams", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getExams() {
        return ResponseEntity.ok(this.examService.findAll());
    }

    /**
     * Creates a new exam in the system.
     * 
     * @param examDTO The exam data transfer object containing exam information
     * @return ResponseEntity containing the created exam with HTTP status 201 (CREATED)
     */
    @PostMapping(value = "/exams", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addExam(@RequestBody ExamDTO examDTO) {
            return ResponseEntity.status(HttpStatus.CREATED).body(this.examService.save(examDTO));        
    }

    /**
     * Retrieves a specific exam by its ID.
     * 
     * @param id The ID of the exam to retrieve
     * @return ResponseEntity containing the exam with HTTP status 200 (OK)
     */
    @GetMapping(value = "/exams/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getExamById(@PathVariable int id) {
        this.examService.findById(id);
        return ResponseEntity.ok(this.examService.findById(id));
    }

    /**
     * Updates a specific exam with new information.
     * 
     * @param id The ID of the exam to update
     * @param examDTO The exam data transfer object containing updated exam information
     * @return ResponseEntity containing the updated exam with HTTP status 200 (OK)
     */
    @PutMapping(value = "/exams/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateExam(@PathVariable int id, @RequestBody ExamDTO examDTO) {
        return ResponseEntity.ok(this.examService.update(id, examDTO));
    }

    /**
     * Retrieves all questions for a specific exam.
     * 
     * @param id The ID of the exam to retrieve questions for
     * @return ResponseEntity containing a list of exam questions with HTTP status 200 (OK)
     */
    @GetMapping(value = "/exams/{id}/questions", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getQuestions(@PathVariable int id) {
        return ResponseEntity.ok(this.examService.findAllQuestionsByExamId(id));
    }

    /**
     * Adds a new question to a specific exam.
     * 
     * @param id The ID of the exam to add a question to
     * @param questionDTO The question data transfer object containing question information
     * @return ResponseEntity containing the added question with HTTP status 201 (CREATED)
     */
    @PostMapping(value="/exams/{id}/questions", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addQuestion(@PathVariable int id, @RequestBody QuestionDTO questionDTO) {
        this.examService.addQuestion(id, questionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(questionDTO);
    }

    /**
     * Deletes questions of a specific exam.
     *
     * @param id The ID of the exam to delete a questions for
     * @return ResponseEntity containing nothing with HTTP status 200 (OK)
     */
    @DeleteMapping(value="/exams/{id}/questions")
    public ResponseEntity deleteQuestion(@PathVariable int id) {
        this.examService.deleteQuestions(id);
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }

     /**
     * Evaluates an exam submission and returns the result.
     * 
     * @param submissionDTO The exam submission data transfer object containing submission information
     * @return ResponseEntity containing the exam result with HTTP status 200 (OK)
     */
    @PostMapping(value = "/exams/submit", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity submitExam(@RequestBody ExamSubmissionDTO submissionDTO) {
        ExamResultDTO result = this.examService.evaluateExam(submissionDTO);
        return ResponseEntity.ok(result);
    }

    /**
     * Starts an exam by its ID and returns a version of the exam with a randomized selection of questions, 
     * limited to the number defined in the exam's configuration. 
     * If the number of questions available is less than or equal to the desired 
     * amount, all questions are returned.
     *
     * @param id the ID of the exam to start
     * @return ResponseEntity containing the exam with randomly selected questions
     */
    @GetMapping(value = "/exams/{id}/start", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity startExam(@PathVariable int id, @RequestParam int userId) {
        return ResponseEntity.ok(this.examService.startExamWithTimer(id, userId));
    }

    @GetMapping(value = "/exams/{id}/time", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getRemainingTime(@PathVariable int id, @RequestParam int userId) {
        int remainingSeconds = this.examService.getRemainingTimeInSeconds(id, userId);
        return ResponseEntity.ok(Map.of("remainingSeconds", remainingSeconds));
    }
}
