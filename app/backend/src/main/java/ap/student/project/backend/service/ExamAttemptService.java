package ap.student.project.backend.service;

import ap.student.project.backend.dao.ExamAttemptRepository;
import ap.student.project.backend.dto.ExamAttemptDTO;
import ap.student.project.backend.entity.ExamAttempt;
import ap.student.project.backend.entity.UserModule;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamAttemptService {
    private final ExamAttemptRepository examAttemptRepository;
    private final UserModuleService userModuleService;

    public ExamAttemptService(ExamAttemptRepository examAttemptRepository, UserModuleService userModuleService) {
        this.examAttemptRepository = examAttemptRepository;
        this.userModuleService = userModuleService;
    }

    public void save(ExamAttemptDTO examAttemptDTO) {
        ExamAttempt examAttempt = new ExamAttempt();
        UserModule userModule = userModuleService.findById(examAttemptDTO.userModuleId());
        examAttempt.setUserModule(userModule);
        BeanUtils.copyProperties(examAttemptDTO, examAttempt);
        examAttemptRepository.save(examAttempt);
    }

    public List<ExamAttempt> findAll() {
        return examAttemptRepository.findAll();
    }
}
