package ap.student.project.backend.controller;

import ap.student.project.backend.entity.UserModule;
import ap.student.project.backend.service.UserModuleService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @GetMapping(value = "/userModules", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUserModules() {
        return ResponseEntity.ok(this.userModuleService.findAll());
    }
}
