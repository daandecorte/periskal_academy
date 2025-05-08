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

@Service
public class PdfService {
    private final UserCertificateService userCertificateService;
    public PdfService(UserCertificateService userCertificateService) {
        this.userCertificateService = userCertificateService;
    }
    public byte[] generatePdf(int userCertificateId) throws IOException {
        UserCertificate userCertificate = this.userCertificateService.findById(userCertificateId);
        Certificate certificate = userCertificate.getCertificate();
        Training training = certificate.getTraining();
        User user = userCertificate.getUser();

        InputStream htmlStream = new ClassPathResource("index.html").getInputStream();
        String html = new String(htmlStream.readAllBytes(), StandardCharsets.UTF_8);

        html = html.replace("${name}", user.getFirstname() + " " + user.getLastname())
                .replace("${training}", training.getTitle().get(Language.ENGLISH))
                .replace("${date}", formatFancyDate(userCertificate.getIssueDate()));

        InputStream imgStream = new ClassPathResource("periskalLogo.jpg").getInputStream();
        byte[] imgBytes = imgStream.readAllBytes();
        String base64 = Base64.getEncoder().encodeToString(imgBytes);
        String imageTag = "<img src=\"data:image/png;base64," + base64 + "\" alt=\"\"/>";
        html = html.replace("<img src=\"periskalLogo.png\" alt=\"\"/>", imageTag);

        System.out.println(html);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.useFastMode();
        builder.useDefaultPageSize(210, 297, PdfRendererBuilder.PageSizeUnits.MM);
        builder.withHtmlContent(html, null);
        builder.toStream(outputStream);
        builder.run();

        return outputStream.toByteArray();
    }
    public static String formatFancyDate(LocalDate date) {
        int day = date.getDayOfMonth();
        String suffix = getDaySuffix(day);
        String month = date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        return day + suffix + " of " + month + ", " + date.getYear();
    }

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
