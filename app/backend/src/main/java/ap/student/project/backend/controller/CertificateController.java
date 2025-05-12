package ap.student.project.backend.controller;

import ap.student.project.backend.dto.CertificateDTO;
import ap.student.project.backend.entity.Certificate;
import ap.student.project.backend.exceptions.MissingArgumentException;
import ap.student.project.backend.exceptions.NotFoundException;
import ap.student.project.backend.service.CertificateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class CertificateController {
    private final CertificateService certificateService;

    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping(value = "/certificates", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCertificates() {
        return ResponseEntity.ok(certificateService.getAllCertificates());
    }

    @PostMapping(value = "/certificates", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addCertificate(@RequestBody CertificateDTO certificateDTO) {
        Certificate certificate = this.certificateService.save(certificateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(certificate);
    }

    @GetMapping(value = "/certificates/{id}")
    public ResponseEntity getCertificate(@PathVariable("id") int id) {
        return ResponseEntity.ok(this.certificateService.findById(id));
    }

    @GetMapping(value = "/certificates/training/{id}")
    public ResponseEntity getCertificateByTrainingId(@PathVariable("id") int id) {
        return ResponseEntity.ok(this.certificateService.findByTrainingId(id));
    }
}
