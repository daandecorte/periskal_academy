package ap.student.project.backend.service;

import ap.student.project.backend.dao.UserModuleRepository;
import ap.student.project.backend.dto.UserModuleDTO;
import ap.student.project.backend.entity.UserModule;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserModuleService {

    private final UserModuleRepository userModuleRepository;

    @Autowired
    public UserModuleService(UserModuleRepository userModuleRepository) {
        this.userModuleRepository = userModuleRepository;
    }

    public void save(UserModuleDTO userModuleDTO) {
        UserModule userModule = new UserModule();
        BeanUtils.copyProperties(userModuleDTO, userModule);
        userModuleRepository.save(userModule);
    }
    public List<UserModule> findAll() {
        return userModuleRepository.findAll();
    }
}
