package ap.student.project.backend.controller;

import ap.student.project.backend.dto.UserDTO;
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
    @GetMapping(value = "/users/periskal_id/{periskalId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUserById(@PathVariable("periskalId") String periskalId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.findByPeriskalId(periskalId));
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
        } catch (DuplicateException e) {
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
        } catch (NotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @GetMapping(value = "/users/{id}/exam_attempts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getExams(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(this.userService.getAllExamAttempts(id));
        } catch (NotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @PutMapping("/users")
    public ResponseEntity update(@RequestBody UserDTO userDTO) {
        try {
            this.userService.update(userDTO);
            return ResponseEntity.ok(userDTO);
        } catch (NotFoundException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }
}
