package ap.student.project.backend.controller;

import ap.student.project.backend.entity.UserModule;
import ap.student.project.backend.service.UserModuleService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
public class UserModuleController {
    private final UserModuleService userModuleService;

    public UserModuleController(UserModuleService userModuleService) {
        this.userModuleService = userModuleService;
    }

    @GetMapping("/userModules")
    public List<UserModule> getUserModules() {
        return this.userModuleService.findAll();
    }
}
