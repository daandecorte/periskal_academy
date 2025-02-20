package ap.student.project.backend.controller;

import ap.student.project.backend.entity.User;
import ap.student.project.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping
    public List<User> getAll() {
        return userService.findAll();
    }
    @PostMapping
    public void create(@RequestBody UserDTO user) {
        this.userService.save(user);
    }
}
