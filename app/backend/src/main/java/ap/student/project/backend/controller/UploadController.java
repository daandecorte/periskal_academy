package ap.student.project.backend.controller;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
public class UploadController {
    private static final String FTP_SERVER = "academyws.periskal.com";
    private static final int FTP_PORT = 9910;
    private static final String FTP_USER = "AcademyWS";
    private static final String FTP_PASSWORD = "Pass4Academy2025!";
    private static final String TARGET_DIRECTORY = "/groep7/";

    @PostMapping("/upload")
    public String uploadToFTP(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "File is empty!";
        }

        FTPSClient ftp = new FTPSClient("SSL", false);
        try {
            ftp.connect(FTP_SERVER, FTP_PORT);
            int reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return "Failed to connect to FTP Server.";
            }

            if (!ftp.login(FTP_USER, FTP_PASSWORD)) {
                ftp.logout();
                return "Login failed.";
            }

            System.out.println("Connected to FTP Server");

            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.execPBSZ(0);
            ftp.execPROT("P");
            ftp.enterLocalPassiveMode();

            if (!ftp.changeWorkingDirectory(TARGET_DIRECTORY)) {
                ftp.makeDirectory(TARGET_DIRECTORY);
                ftp.changeWorkingDirectory(TARGET_DIRECTORY);
            }

            try (InputStream inputStream = file.getInputStream()) {
                boolean uploaded = ftp.storeFile(file.getOriginalFilename(), inputStream);
                if (!uploaded) {
                    return "Failed to upload file to FTP.";
                }
            }

            ftp.logout();
            return "https://academyws.periskal.com/Downloads/groep7/"+file.getOriginalFilename();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        } finally {
            try {
                ftp.disconnect();
            } catch (IOException ignored) {
            }
        }
    }
}