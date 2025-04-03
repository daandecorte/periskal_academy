package ap.student.project.backend.service;

import ap.student.project.backend.dao.UserModuleRepository;
import ap.student.project.backend.dto.UserModuleDTO;
import ap.student.project.backend.entity.Module;
import ap.student.project.backend.entity.User;
import ap.student.project.backend.entity.UserModule;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserModuleService {

    private final UserModuleRepository userModuleRepository;
    private final UserService userService;
    private final ModuleService moduleService;

    @Autowired
    public UserModuleService(UserModuleRepository userModuleRepository, UserService userService, ModuleService moduleService) {
        this.userModuleRepository = userModuleRepository;
        this.userService = userService;
        this.moduleService = moduleService;
    }

    public void save(UserModuleDTO userModuleDTO) {
        UserModule userModule = new UserModule();
        if(userModuleDTO.user_id()==0) {
            throw new MissingArgumentException("user_id is missing");
        }
        if(userModuleDTO.module_id()==0) {
            throw new MissingArgumentException("module_id is missing");
        }
        User user = userService.findById(userModuleDTO.user_id());
        Module module = moduleService.findById(userModuleDTO.module_id());
        userModule.setModule(module);
        userModule.setUser(user);
        userModuleRepository.save(userModule);
    }
    public UserModule findById(int id) throws NotFoundException {
        UserModule userModule = this.userModuleRepository.findById(id).orElse(null);
        if (userModule == null) {
            throw new NotFoundException("User Module With Id " + id + " Not Found");
        }
        return userModule;
    }
    public List<UserModule> findAll() {
        return userModuleRepository.findAll();
    }
}
