package ap.student.project.backend.controller;

import ap.student.project.backend.dto.UserCertificateDTO;
import ap.student.project.backend.entity.UserCertificate;
import ap.student.project.backend.service.UserCertificateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for handling user certificate-related HTTP requests.
 * Manages operations for user certificates including creation and retrieval
 * by various criteria.
 */
@RestController
@CrossOrigin
public class UserCertificateController {
    private final UserCertificateService userCertificateService;

    public UserCertificateController(UserCertificateService userCertificateService) {
        this.userCertificateService = userCertificateService;
    }

/**
     * Retrieves all user certificates from the system.
     * 
     * @return ResponseEntity containing a list of all user certificates with HTTP status 200 (OK)
     */
    @GetMapping(value = "/user_certificates", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllUserCertificates() {
        return ResponseEntity.ok(this.userCertificateService.findAll());
    }

    /**
     * Retrieves a specific user certificate by its ID.
     * 
     * @param id The ID of the user certificate to retrieve
     * @return ResponseEntity containing the user certificate with HTTP status 200 (OK)
     */
    @GetMapping(value = "/user_certificates/{id}")
    public ResponseEntity getUserCertificate(@PathVariable("id") int id) {
        return ResponseEntity.ok(this.userCertificateService.findById(id));
    }

    /**
     * Retrieves all user certificates for a specific user.
     * 
     * @param id The user ID to find certificates for
     * @return ResponseEntity containing certificates associated with the user ID with HTTP status 200 (OK)
     */
    @GetMapping(value = "/user_certificates/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCertificateByUserId(@PathVariable("id") int id) {
        return ResponseEntity.ok(this.userCertificateService.findByUserId(id));
    }

    /**
     * Creates a new user certificate in the system.
     * 
     * @param userCertificateDTO The user certificate data transfer object containing certificate information
     * @return ResponseEntity containing the created user certificate with HTTP status 201 (CREATED)
     */
    @PostMapping(value = "/user_certificates", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createUserCertificate(@RequestBody UserCertificateDTO userCertificateDTO) {
        UserCertificate userCertificate = this.userCertificateService.save(userCertificateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCertificate);
    }

    @GetMapping(value="/user_certificates/training/{trainingId}/user/{userId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUserCertificate(@PathVariable("trainingId") int trainingId, @PathVariable("userId") int userId) {
        return ResponseEntity.ok(this.userCertificateService.findByTrainingIdAndUserId(trainingId, userId));
    }
}
