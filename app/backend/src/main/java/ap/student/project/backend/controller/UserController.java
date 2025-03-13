package ap.student.project.backend.controller;

import ap.student.project.backend.dto.UserDTO;
import ap.student.project.backend.entity.Language;
import ap.student.project.backend.entity.User;
import ap.student.project.backend.entity.UserExam;
import ap.student.project.backend.entity.UserModule;
import ap.student.project.backend.exceptions.DuplicateException;
import ap.student.project.backend.exceptions.NotFoundException;
import ap.student.project.backend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@CrossOrigin
@RestController
public class UserController {
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUserById(@PathVariable("id") int id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.findById(id));
        } catch (NotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@RequestBody UserDTO user) {
        try {
            this.userService.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.assemble(user));
        }
        catch (DuplicateException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @DeleteMapping(value = "/users/{id}")
    public ResponseEntity delete(@PathVariable("id") int id) {
        this.userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping(value = "/users/{id}/modules", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getModules(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(this.userService.getAllUserModules(id));
        }
        catch (NotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }
    @GetMapping(value = "/users/{id}/exams", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getExams(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(this.userService.getAllUserExams(id));
        }
        catch (NotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }
    @PostMapping(value="/users/{id}/modules", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addUserModule(@PathVariable("id") int id, @RequestParam int moduleId) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.addUserModule(id, moduleId));
        }
        catch (NotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (DuplicateException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PostMapping(value="/users/{id}/exams", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addUserExam(@PathVariable("id") int id, @RequestParam int examId) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.addUserExam(id, examId));
        }
        catch (NotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (DuplicateException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @PutMapping("/users/{id}")
    public ResponseEntity update(@PathVariable int id ,@RequestBody User user) {
        User newUser = this.userService.findById(id);
        newUser.setLanguage(user.getLanguage());
        this.userService.update(newUser);
        return ResponseEntity.ok(newUser);
    }
}
