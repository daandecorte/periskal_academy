package ap.student.project.backend.service;

import ap.student.project.backend.dao.UserExamRepository;
import ap.student.project.backend.dto.UserExamDTO;
import ap.student.project.backend.entity.UserExam;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserExamService {
    private final UserExamRepository userExamRepository;

    public UserExamService(UserExamRepository userExamRepository) {
        this.userExamRepository = userExamRepository;
    }

    public void save(UserExamDTO userExamDTO) {
        UserExam userExam = new UserExam();
        BeanUtils.copyProperties(userExamDTO, userExam);
        userExamRepository.save(userExam);
    }

    public List<UserExam> findAll() {
        return userExamRepository.findAll();
    }
}
