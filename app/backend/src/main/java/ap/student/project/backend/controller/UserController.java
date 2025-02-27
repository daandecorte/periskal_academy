package ap.student.project.backend.controller;

import ap.student.project.backend.dto.UserDTO;
import ap.student.project.backend.entity.User;
import ap.student.project.backend.entity.UserExam;
import ap.student.project.backend.entity.UserModule;
import ap.student.project.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@CrossOrigin
@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAll() {
        return userService.findAll();
    }

    @GetMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserById(@PathVariable("id") int id) {
        return userService.findById(id);
    }

    @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public User create(@RequestBody UserDTO user) {
        this.userService.save(user);
        return userService.assembleUser(user);
    }

    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") int id) {
        this.userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping(value = "/users/{id}/modules", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserModule> getModules(@PathVariable("id") int id) {
        try {
            return this.userService.getAllUserModules(id);
        }
        catch (Exception e) {
            return Collections.emptyList();
        }

    }
    @GetMapping(value = "/users/{id}/exams", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserExam> getExams(@PathVariable("id") int id) {
        try {
            return this.userService.getAllUserExams(id);
        }
        catch (Exception e) {
            return Collections.emptyList();
        }

    }
    @PostMapping(value="/users/{id}/modules", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserModule addUserModule(@PathVariable("id") int id, @RequestParam int moduleId) {
        try {
            System.out.println(moduleId);
            return this.userService.addUserModule(id, moduleId);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
