package ap.student.project.backend.controller;

import ap.student.project.backend.dto.UserExamDTO;
import ap.student.project.backend.entity.UserExam;
import ap.student.project.backend.service.UserExamService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class UserExamController {
    private final UserExamService userExamService;

    public UserExamController(UserExamService userExamService) {
        this.userExamService = userExamService;
    }

    @GetMapping("/userExams")
    public List<UserExam> getExams() {
        return this.userExamService.findAll();
    }

    @PostMapping("/userExams")
    public UserExam addModule(@RequestBody UserExamDTO userExamDTO) {
        UserExam userExam = new UserExam();
        BeanUtils.copyProperties(userExamDTO, userExam);
        this.userExamService.save(userExamDTO);
        return userExam;
    }
}
