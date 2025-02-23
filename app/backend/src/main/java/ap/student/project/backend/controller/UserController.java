package ap.student.project.backend.controller;

import ap.student.project.backend.entity.User;
import ap.student.project.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value= "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAll() {
        return userService.findAll();
    }
    @GetMapping(value="/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserById(@PathVariable("id") Long id) {
        return userService.findById(id);
    }

    @PostMapping(value="/user")
    public void create(@RequestBody UserDTO user) {
        this.userService.save(user);
    }
}
