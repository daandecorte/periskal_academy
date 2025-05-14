package ap.student.project.backend.controller;

import ap.student.project.backend.dto.UserDTO;
import ap.student.project.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for handling user-related HTTP requests.
 * Manages operations for users including creation, retrieval by various criteria,
 * updates, and access to user-related data such as trainings and exam attempts.
 */
@CrossOrigin
@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

/**
     * Retrieves all users from the system.
     * 
     * @return ResponseEntity containing a list of all users with HTTP status 200 (OK)
     */
    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    /**
     * Retrieves a specific user by their ID.
     * 
     * @param id The ID of the user to retrieve
     * @return ResponseEntity containing the user with HTTP status 200 (OK)
     */
    @GetMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUserById(@PathVariable("id") int id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(id));
    }

    /**
     * Retrieves a specific user by their Periskal ID.
     * 
     * @param periskalId The Periskal ID of the user to retrieve
     * @return ResponseEntity containing the user with HTTP status 200 (OK)
     */
    @GetMapping(value = "/users/periskal_id/{periskalId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUserById(@PathVariable("periskalId") String periskalId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findByPeriskalId(periskalId));
    }

    /**
     * Creates a new user in the system.
     * 
     * @param user The user data transfer object containing user information
     * @return ResponseEntity containing the created user with HTTP status 201 (CREATED)
     */
    @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@RequestBody UserDTO user) {
        this.userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.assemble(user));
    }

    /**
     * Retrieves all trainings associated with a specific user.
     * 
     * @param id The ID of the user to retrieve trainings for
     * @return ResponseEntity containing a list of user trainings with HTTP status 200 (OK)
     */
    @GetMapping(value = "/users/{id}/trainings", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getTrainings(@PathVariable("id") int id) {
        return ResponseEntity.ok(this.userService.getAllUserModules(id));
    }

    /**
     * Retrieves all exam attempts for a specific user.
     * 
     * @param id The ID of the user to retrieve exam attempts for
     * @return ResponseEntity containing a list of user exam attempts with HTTP status 200 (OK)
     */
    @GetMapping(value = "/users/{id}/exam_attempts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getExamAttempts(@PathVariable("id") int id) {
        return ResponseEntity.ok(this.userService.getAllExamAttempts(id));
    }

    /**
     * Retrieves chat member information for a specific user.
     * 
     * @param id The ID of the user to retrieve chat member information for
     * @return ResponseEntity containing the user's chat member information with HTTP status 200 (OK)
     */
    @GetMapping(value = "/users/{id}/chat_member", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getChatMember(@PathVariable("id") int id) {
        return ResponseEntity.ok(this.userService.getChatMember(id));
    }

    /**
     * Updates a specific user with new information.
     * 
     * @param userDTO The user data transfer object containing updated user information
     * @return ResponseEntity containing the updated user with HTTP status 200 (OK)
     */
    @PutMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity update(@RequestBody UserDTO userDTO) {
        this.userService.update(userDTO);
        return ResponseEntity.ok(userDTO);
    }
}