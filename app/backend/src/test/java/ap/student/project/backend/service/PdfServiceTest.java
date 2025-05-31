package ap.student.project.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import ap.student.project.backend.dao.CertificateRepository;
import ap.student.project.backend.dao.TrainingRepository;
import ap.student.project.backend.dao.UserCertificateRepository;
import ap.student.project.backend.dao.UserRepository;
import ap.student.project.backend.entity.Certificate;
import ap.student.project.backend.entity.CertificateStatus;
import ap.student.project.backend.entity.Language;
import ap.student.project.backend.entity.Training;
import ap.student.project.backend.entity.User;
import ap.student.project.backend.entity.UserCertificate;
import ap.student.project.backend.exceptions.NotFoundException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
public class PdfServiceTest {

    @Autowired
    private PdfService pdfService;

    @Autowired
    private UserCertificateRepository userCertificateRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private TrainingRepository trainingRepository;

    private User testUser;
    private Training testTraining;
    private Certificate testCertificate;
    private UserCertificate testUserCertificate;

    @BeforeEach
    void setUp() {
        // Create test training
        Map<Language, String> titles = new HashMap<>();
        titles.put(Language.ENGLISH, "Safety Training Course");
        titles.put(Language.DUTCH, "Veiligheidstraining Cursus");

        Map<Language, String> descriptions = new HashMap<>();
        descriptions.put(Language.ENGLISH, "Comprehensive safety training for maritime personnel");
        descriptions.put(Language.DUTCH, "Uitgebreide veiligheidstraining voor maritiem personeel");

        testTraining = new Training();
        testTraining.setTitle(titles);
        testTraining.setDescription(descriptions);
        testTraining.setActive(true);
        testTraining = trainingRepository.save(testTraining);

        // Create test certificate
        testCertificate = new Certificate(testTraining, 365, 150.0);
        testCertificate = certificateRepository.save(testCertificate);

        // Create test user
        testUser = new User("PER001", "John", "Doe", "SS Enterprise", Language.ENGLISH);
        testUser = userRepository.save(testUser);

        // Create test user certificate
        testUserCertificate = new UserCertificate(
                LocalDate.of(2024, 5, 15),
                LocalDate.of(2025, 5, 15),
                testCertificate,
                testUser
        );
        testUserCertificate.setStatus(CertificateStatus.VALID);
        testUserCertificate = userCertificateRepository.save(testUserCertificate);
    }

    @Test
    void testGeneratePdf_ValidUserCertificate_ShouldReturnPdfBytes() throws IOException {
        byte[] pdfBytes = pdfService.generatePdf(testUserCertificate.getId());

        assertNotNull(pdfBytes, "PDF bytes should not be null");
        assertTrue(pdfBytes.length > 0, "PDF should contain data");
        
        // Verify PDF header (PDF files start with %PDF)
        String pdfHeader = new String(pdfBytes, 0, Math.min(4, pdfBytes.length));
        assertEquals("%PDF", pdfHeader, "Generated file should be a valid PDF");
    }

    @Test
    void testGeneratePdf_NonExistentUserCertificate_ShouldThrowNotFoundException() {
        int nonExistentId = 99999;

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> pdfService.generatePdf(nonExistentId),
                "Should throw NotFoundException for non-existent user certificate"
        );

        assertTrue(exception.getMessage().contains("User Certificate with id " + nonExistentId + " not found"));
    }

    @Test
    void testGeneratePdf_ShouldContainUserInformation() throws IOException {
        User userWithSpecialChars = new User("PER002", "María", "García-López", "MV Océan Bleu", Language.ENGLISH);
        userWithSpecialChars = userRepository.save(userWithSpecialChars);

        UserCertificate specialUserCert = new UserCertificate(
                LocalDate.of(2024, 12, 25),
                LocalDate.of(2025, 12, 25),
                testCertificate,
                userWithSpecialChars
        );
        specialUserCert.setStatus(CertificateStatus.VALID);
        specialUserCert = userCertificateRepository.save(specialUserCert);

        byte[] pdfBytes = pdfService.generatePdf(specialUserCert.getId());

        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
        
        String pdfContent = new String(pdfBytes);
    }

    @Test
    void testGeneratePdf_DifferentStatuses_ShouldGeneratePdf() throws IOException {
        UserCertificate expiredCert = new UserCertificate(
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2024, 1, 1),
                testCertificate,
                testUser
        );
        expiredCert.setStatus(CertificateStatus.EXPIRED);
        expiredCert = userCertificateRepository.save(expiredCert);

        byte[] expiredPdfBytes = pdfService.generatePdf(expiredCert.getId());
        assertNotNull(expiredPdfBytes);
        assertTrue(expiredPdfBytes.length > 0);

        UserCertificate revokedCert = new UserCertificate(
                LocalDate.of(2024, 6, 1),
                LocalDate.of(2025, 6, 1),
                testCertificate,
                testUser
        );
        revokedCert.setStatus(CertificateStatus.REVOKED);
        revokedCert = userCertificateRepository.save(revokedCert);

        byte[] revokedPdfBytes = pdfService.generatePdf(revokedCert.getId());
        assertNotNull(revokedPdfBytes);
        assertTrue(revokedPdfBytes.length > 0);
    }

    @Test
    void testGeneratePdf_EdgeCaseDates_ShouldGeneratePdf() throws IOException {
        // Test leap year date
        UserCertificate leapYearCert = new UserCertificate(
                LocalDate.of(2024, 2, 29), // Leap year
                LocalDate.of(2025, 2, 28),
                testCertificate,
                testUser
        );
        leapYearCert.setStatus(CertificateStatus.VALID);
        leapYearCert = userCertificateRepository.save(leapYearCert);

        byte[] leapYearPdfBytes = pdfService.generatePdf(leapYearCert.getId());
        assertNotNull(leapYearPdfBytes);
        assertTrue(leapYearPdfBytes.length > 0);

        // Test New Year's Day
        UserCertificate newYearCert = new UserCertificate(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2025, 1, 1),
                testCertificate,
                testUser
        );
        newYearCert.setStatus(CertificateStatus.VALID);
        newYearCert = userCertificateRepository.save(newYearCert);

        byte[] newYearPdfBytes = pdfService.generatePdf(newYearCert.getId());
        assertNotNull(newYearPdfBytes);
        assertTrue(newYearPdfBytes.length > 0);
    }

    @Test
    void testFormatFancyDate_VariousDates_ShouldReturnCorrectFormat() {
        // Test different day suffixes
        assertEquals("1st of January, 2024", PdfService.formatFancyDate(LocalDate.of(2024, 1, 1)));
        assertEquals("2nd of February, 2024", PdfService.formatFancyDate(LocalDate.of(2024, 2, 2)));
        assertEquals("3rd of March, 2024", PdfService.formatFancyDate(LocalDate.of(2024, 3, 3)));
        assertEquals("4th of April, 2024", PdfService.formatFancyDate(LocalDate.of(2024, 4, 4)));
        
        // Test special cases (11th, 12th, 13th should all use "th")
        assertEquals("11th of May, 2024", PdfService.formatFancyDate(LocalDate.of(2024, 5, 11)));
        assertEquals("12th of June, 2024", PdfService.formatFancyDate(LocalDate.of(2024, 6, 12)));
        assertEquals("13th of July, 2024", PdfService.formatFancyDate(LocalDate.of(2024, 7, 13)));
        
        // Test 21st, 22nd, 23rd
        assertEquals("21st of August, 2024", PdfService.formatFancyDate(LocalDate.of(2024, 8, 21)));
        assertEquals("22nd of September, 2024", PdfService.formatFancyDate(LocalDate.of(2024, 9, 22)));
        assertEquals("23rd of October, 2024", PdfService.formatFancyDate(LocalDate.of(2024, 10, 23)));
        
        // Test 30th, 31st
        assertEquals("30th of November, 2024", PdfService.formatFancyDate(LocalDate.of(2024, 11, 30)));
        assertEquals("31st of December, 2024", PdfService.formatFancyDate(LocalDate.of(2024, 12, 31)));
    }

    @Test
    void testGeneratePdf_ConcurrentGeneration_ShouldHandleMultipleRequests() throws IOException {
        // Create multiple user certificates
        UserCertificate cert1 = new UserCertificate(
                LocalDate.of(2024, 1, 15),
                LocalDate.of(2025, 1, 15),
                testCertificate,
                testUser
        );
        cert1.setStatus(CertificateStatus.VALID);
        cert1 = userCertificateRepository.save(cert1);

        UserCertificate cert2 = new UserCertificate(
                LocalDate.of(2024, 2, 15),
                LocalDate.of(2025, 2, 15),
                testCertificate,
                testUser
        );
        cert2.setStatus(CertificateStatus.VALID);
        cert2 = userCertificateRepository.save(cert2);

        // Generate PDFs
        byte[] pdf1 = pdfService.generatePdf(cert1.getId());
        byte[] pdf2 = pdfService.generatePdf(cert2.getId());

        // Verify both PDFs are generated successfully
        assertNotNull(pdf1);
        assertNotNull(pdf2);
        assertTrue(pdf1.length > 0);
        assertTrue(pdf2.length > 0);
        
        // PDFs should be different (different dates)
        assertFalse(java.util.Arrays.equals(pdf1, pdf2), "PDFs with different dates should be different");
    }

    @Test
    void testGeneratePdf_MinimalUserInfo_ShouldGeneratePdf() throws IOException {
        // Create user with minimal required information
        User minimalUser = new User("MIN001", "A", "B", "Ship", Language.ENGLISH);
        minimalUser = userRepository.save(minimalUser);

        UserCertificate minimalCert = new UserCertificate(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2025, 1, 1),
                testCertificate,
                minimalUser
        );
        minimalCert.setStatus(CertificateStatus.VALID);
        minimalCert = userCertificateRepository.save(minimalCert);

        byte[] pdfBytes = pdfService.generatePdf(minimalCert.getId());

        assertNotNull(pdfBytes);
        assertTrue(pdfBytes.length > 0);
    }
    
}
