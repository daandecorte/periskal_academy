package ap.student.project.backend.service;

import ap.student.project.backend.dao.ExamAttemptRepository;
import ap.student.project.backend.dto.ExamAttemptDTO;
import ap.student.project.backend.entity.ExamAttempt;
import ap.student.project.backend.entity.UserTraining;
import ap.student.project.backend.exceptions.MissingArgumentException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamAttemptService {
    private final ExamAttemptRepository examAttemptRepository;
    private final UserTrainingService userTrainingService;

    public ExamAttemptService(ExamAttemptRepository examAttemptRepository, UserTrainingService userTrainingService) {
        this.examAttemptRepository = examAttemptRepository;
        this.userTrainingService = userTrainingService;
    }

    public ExamAttempt save(ExamAttemptDTO examAttemptDTO) {
        ExamAttempt examAttempt = new ExamAttempt();
        if(examAttemptDTO.userTrainingId()==0) {
            throw new MissingArgumentException("user_training_id is missing");
        }
        UserTraining userTraining = userTrainingService.findById(examAttemptDTO.userTrainingId());
        examAttempt.setUserTraining(userTraining);
        BeanUtils.copyProperties(examAttemptDTO, examAttempt);
        return examAttemptRepository.save(examAttempt);
    }

    public List<ExamAttempt> findAll() {
        return examAttemptRepository.findAll();
    }
}
