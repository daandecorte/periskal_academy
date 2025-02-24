package ap.student.project.backend.controller;

import ap.student.project.backend.entity.User;
import ap.student.project.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getAll() {
        return userService.findAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getUserById(@PathVariable("id") Long id) {
        return userService.findById(id);
    }

    @PostMapping(value = "/")
    public User create(@RequestBody UserDTO user) {
        this.userService.save(user);
        return userService.assembleUser(user);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        this.userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
