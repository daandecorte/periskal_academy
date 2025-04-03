package ap.student.project.backend.controller;

import ap.student.project.backend.dto.UserModuleDTO;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import ap.student.project.backend.service.UserModuleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class UserModuleController {
    private final UserModuleService userModuleService;

    public UserModuleController(UserModuleService userModuleService) {
        this.userModuleService = userModuleService;
    }

    @GetMapping(value = "/user_modules", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUserModules() {
        return ResponseEntity.ok(this.userModuleService.findAll());
    }

    @PostMapping(value = "/user_modules", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createUserModule(@RequestBody UserModuleDTO userModuleDTO) {
        try {
            this.userModuleService.save(userModuleDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(userModuleDTO);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (MissingArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
