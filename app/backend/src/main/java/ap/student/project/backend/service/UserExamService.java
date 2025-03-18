package ap.student.project.backend.service;

import ap.student.project.backend.dao.UserExamRepository;
import ap.student.project.backend.dto.UserExamDTO;
import ap.student.project.backend.dto.UserModuleDTO;
import ap.student.project.backend.entity.*;
import ap.student.project.backend.entity.Module;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserExamService {
    private final UserExamRepository userExamRepository;
    private final UserService userService;
    private final ExamService examService;

    public UserExamService(UserExamRepository userExamRepository, UserService userService, ExamService examService) {
        this.userExamRepository = userExamRepository;
        this.userService = userService;
        this.examService = examService;
    }

    public void save(UserExamDTO userExamDTO) {
        UserExam userExam = new UserExam();
        User user = userService.findById(userExamDTO.user_id());
        Exam exam = examService.findById(userExamDTO.exam_id());
        userExam.setExam(exam);
        userExam.setUser(user);
        userExamRepository.save(userExam);
    }

    public List<UserExam> findAll() {
        return userExamRepository.findAll();
    }
}
