package ap.student.project.backend.service;
/*
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
    private final ExamService examService;
    private final UserModuleService userModuleService;

    public UserExamService(UserExamRepository userExamRepository, ExamService examService, UserModuleService userModuleService) {
        this.userExamRepository = userExamRepository;
        this.examService = examService;
        this.userModuleService = userModuleService;
    }

    public void save(UserExamDTO userExamDTO) {
        UserExam userExam = new UserExam();
        UserModule userModule = userModuleService.findById(userExamDTO.user_module_id());
        Exam exam = examService.findById(userExamDTO.exam_id());
        userExam.setExam(exam);
        userExam.setUserModule(userModule);
        userExamRepository.save(userExam);
    }

    public List<UserExam> findAll() {
        return userExamRepository.findAll();
    }
}
*/