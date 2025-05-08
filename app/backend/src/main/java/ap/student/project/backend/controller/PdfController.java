package ap.student.project.backend.controller;

import ap.student.project.backend.service.PdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/pdf")
public class PdfController {
    private final PdfService pdfService;
    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }
    @GetMapping("/generate/{id}")
    public ResponseEntity generatePdf(@PathVariable("id") int userCertificateId) {
        try {
            byte[] pdf = this.pdfService.generatePdf(userCertificateId);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=document.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        }
        catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}
