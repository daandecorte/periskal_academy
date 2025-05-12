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
        return ResponseEntity.ok(this.userCertificateService.findById(id));
    }
    @GetMapping(value = "/user-certificates/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCertificateByUserId(@PathVariable("id") int id) {
        return ResponseEntity.ok(this.userCertificateService.findByUserId(id));
    }
    @PostMapping(value = "/user-certificates", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createUserCertificate(@RequestBody UserCertificateDTO userCertificateDTO) {
        UserCertificate userCertificate = this.userCertificateService.save(userCertificateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCertificate);
    }
}
