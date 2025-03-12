package ap.student.project.backend.controller;

import ap.student.project.backend.dto.ExamDTO;
import ap.student.project.backend.entity.Exam;
import ap.student.project.backend.service.ExamService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class ExamController {
    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @GetMapping("/exams")
    public List<Exam> getExams() {
        return this.examService.findAll();
    }

    @PostMapping("/exams")
    public Exam addModule(@RequestBody ExamDTO examDTO) {
        Exam exam = new Exam();
        BeanUtils.copyProperties(examDTO, exam);
        this.examService.save(examDTO);
        return exam;
    }
}
