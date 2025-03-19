package ap.student.project.backend.controller;

import ap.student.project.backend.dto.UserExamDTO;
import ap.student.project.backend.exceptions.NotFoundException;
import ap.student.project.backend.service.UserExamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class UserExamController {
    private final UserExamService userExamService;

    public UserExamController(UserExamService userExamService) {
        this.userExamService = userExamService;
    }

    @GetMapping(value = "/userExams", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getExams() {
        return ResponseEntity.ok(this.userExamService.findAll());
    }

    @PostMapping(value = "/userExams", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createUserExam(@RequestBody UserExamDTO userExamDTO) {
        try {
            this.userExamService.save(userExamDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(userExamDTO);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
