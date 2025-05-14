package ap.student.project.backend.controller;

import ap.student.project.backend.dto.CertificateDTO;
import ap.student.project.backend.entity.Certificate;
import ap.student.project.backend.service.CertificateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsible for handling certificate-related HTTP requests.
 * Manages operations for certificates in the system including retrieval, creation,
 * and finding certificates by various criteria.
 */
@RestController
@CrossOrigin
public class CertificateController {
    private final CertificateService certificateService;

    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    /**
     * Retrieves all certificates from the system.
     * 
     * @return ResponseEntity containing a list of all certificates with HTTP status 200 (OK)
     */
    @GetMapping(value = "/certificates", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCertificates() {
        return ResponseEntity.ok(certificateService.getAllCertificates());
    }

    /**
     * Creates a new certificate in the system.
     * 
     * @param certificateDTO The certificate data transfer object containing certificate information
     * @return ResponseEntity containing the created certificate with HTTP status 201 (CREATED)
     */
    @PostMapping(value = "/certificates", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addCertificate(@RequestBody CertificateDTO certificateDTO) {
        Certificate certificate = this.certificateService.save(certificateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(certificate);
    }

    /**
     * Retrieves a specific certificate by its ID.
     * 
     * @param id The ID of the certificate to retrieve
     * @return ResponseEntity containing the certificate with HTTP status 200 (OK)
     */
    @GetMapping(value = "/certificates/{id}")
    public ResponseEntity getCertificate(@PathVariable("id") int id) {
        return ResponseEntity.ok(this.certificateService.findById(id));
    }

    /**
     * Retrieves certificates associated with a specific training ID.
     * 
     * @param id The training ID to find certificates for
     * @return ResponseEntity containing certificates associated with the training ID with HTTP status 200 (OK)
     */
    @GetMapping(value = "/certificates/training/{id}")
    public ResponseEntity getCertificateByTrainingId(@PathVariable("id") int id) {
        return ResponseEntity.ok(this.certificateService.findByTrainingId(id));
    }
}
