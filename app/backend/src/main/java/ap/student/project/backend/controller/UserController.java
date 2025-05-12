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
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(id));
    }
    @GetMapping(value = "/users/periskal_id/{periskalId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUserById(@PathVariable("periskalId") String periskalId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findByPeriskalId(periskalId));
    }

    @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@RequestBody UserDTO user) {
        this.userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.assemble(user));
    }

    @GetMapping(value = "/users/{id}/trainings", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTrainings(@PathVariable("id") int id) {
        return ResponseEntity.ok(this.userService.getAllUserModules(id));
    }

    @GetMapping(value = "/users/{id}/exam_attempts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getExamAttempts(@PathVariable("id") int id) {
        return ResponseEntity.ok(this.userService.getAllExamAttempts(id));
    }
    @GetMapping(value = "/users/{id}/chat_member", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getChatMember(@PathVariable("id") int id) {
        return ResponseEntity.ok(this.userService.getChatMember(id));
    }

    @PutMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(@RequestBody UserDTO userDTO) {
        this.userService.update(userDTO);
        return ResponseEntity.ok(userDTO);
    }
}
