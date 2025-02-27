package ap.student.project.backend.service;

import ap.student.project.backend.dao.UserModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserModuleService {

    private final UserModuleRepository userModuleRepository;

    @Autowired
    public UserModuleService(UserModuleRepository userModuleRepository) {
        this.userModuleRepository = userModuleRepository;
    }
}
