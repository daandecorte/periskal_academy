package ap.student.project.backend.controller;

import ap.student.project.backend.service.PdfService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Controller responsible for handling PDF generation requests.
 * Manages operations for generating PDF documents from certificate data.
 */
@RestController
@RequestMapping("/pdf")
public class PdfController {
    private final PdfService pdfService;
    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }
    /**
     * Generates a PDF document for a specific user certificate.
     * 
     * @param userCertificateId The ID of the user certificate to generate a PDF for
     * @return ResponseEntity containing the generated PDF as byte array with HTTP status 200 (OK)
     *         or HTTP status 500 (INTERNAL_SERVER_ERROR) if PDF generation fails
     */
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
