package ap.student.project.backend.controller;

import ap.student.project.backend.dto.UserCertificateDTO;
import ap.student.project.backend.entity.UserCertificate;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import ap.student.project.backend.service.UserCertificateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class UserCertificateController {
    private final UserCertificateService userCertificateService;
    public UserCertificateController(UserCertificateService userCertificateService) {
        this.userCertificateService = userCertificateService;
    }
    @GetMapping(value = "/user-certificates", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllUserCertificates() {
            return ResponseEntity.ok(this.userCertificateService.findAll());
    }
    @GetMapping(value = "/user-certificates/{id}")
    public ResponseEntity getUserCertificate(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(this.userCertificateService.findById(id));
        }
        catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @GetMapping(value = "/user-certificates/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCertificateByUserId(@PathVariable("id") int id) {
        try {
            return ResponseEntity.ok(this.userCertificateService.findByUserId(id));
        }
        catch(NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PostMapping(value = "/user-certificates", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createUserCertificate(@RequestBody UserCertificateDTO userCertificateDTO) {
        try {
            UserCertificate userCertificate = this.userCertificateService.save(userCertificateDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(userCertificate);
        }
        catch(NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch(MissingArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
