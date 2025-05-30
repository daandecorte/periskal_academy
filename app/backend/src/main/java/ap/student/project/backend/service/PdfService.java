package ap.student.project.backend.service;

import ap.student.project.backend.entity.*;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Base64;
import java.util.Locale;

/**
 * Service class for generating PDF certificates.
 * Handles creating PDF certificate documents for users.
 */
@Service
public class PdfService {
    private final UserCertificateService userCertificateService;

    /**
     * Constructs a new PdfService with the required services.
     *
     * @param userCertificateService Service for user certificate-related operations
     */
    public PdfService(UserCertificateService userCertificateService) {
        this.userCertificateService = userCertificateService;
    }

    /**
     * Generates a PDF certificate for a specific user certificate.
     *
     * @param userCertificateId The ID of the user certificate to generate a PDF for
     * @return The generated PDF as a byte array
     * @throws IOException If there is an error reading template files or generating the PDF
     */
    public byte[] generatePdf(int userCertificateId) throws IOException {
        UserCertificate userCertificate = this.userCertificateService.findById(userCertificateId);
        Certificate certificate = userCertificate.getCertificate();
        Training training = certificate.getTraining();
        User user = userCertificate.getUser();

        InputStream htmlStream = new ClassPathResource("index.html").getInputStream();
        String html = new String(htmlStream.readAllBytes(), StandardCharsets.UTF_8);

        html = html.replace("${name}", user.getFirstname() + " " + user.getLastname())
                .replace("${shipname}", user.getShipname())
                .replace("${training}", training.getTitle().get(Language.ENGLISH))
                .replace("${date}", formatFancyDate(userCertificate.getIssueDate()));

        InputStream imgStream = new ClassPathResource("periskalLogo.jpg").getInputStream();
        byte[] imgBytes = imgStream.readAllBytes();
        String base64 = Base64.getEncoder().encodeToString(imgBytes);
        String imageTag = "<img src=\"data:image/png;base64," + base64 + "\" alt=\"\"/>";
        html = html.replace("<img src=\"periskalLogo.png\" alt=\"\"/>", imageTag);

        imgStream = new ClassPathResource("handtekening.jpg").getInputStream();
        imgBytes = imgStream.readAllBytes();
        base64 = Base64.getEncoder().encodeToString(imgBytes);
        imageTag = "<img src=\"data:image/png;base64," + base64 + "\" alt=\"\"/>";
        html = html.replace("<img src=\"handtekening.jpg\" alt=\"\"/>", imageTag);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.useFastMode();
        builder.useDefaultPageSize(210, 297, PdfRendererBuilder.PageSizeUnits.MM);
        builder.withHtmlContent(html, null);
        builder.toStream(outputStream);
        builder.run();

        return outputStream.toByteArray();
    }

    /**
     * Formats a date in a fancy style for display on certificates.
     *
     * @param date The date to format
     * @return The formatted date string (e.g., "15th of May, 2025")
     */
    public static String formatFancyDate(LocalDate date) {
        int day = date.getDayOfMonth();
        String suffix = getDaySuffix(day);
        String month = date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        return day + suffix + " of " + month + ", " + date.getYear();
    }

    /**
     * Gets the appropriate suffix for a day number (st, nd, rd, th).
     *
     * @param day The day number
     * @return The appropriate suffix string
     */
    private static String getDaySuffix(int day) {
        if (day >= 11 && day <= 13) return "th";
        switch (day % 10) {
            case 1: return "st";
            case 2: return "nd";
            case 3: return "rd";
            default: return "th";
        }
    }
}
