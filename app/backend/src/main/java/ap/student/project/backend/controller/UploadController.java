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

/**
 * Controller responsible for handling file upload operations.
 * This controller provides an endpoint for uploading files to a FTP server
 * using FTPS (FTP over SSL) protocol. Files are stored in a designated directory
 * on the server and can be accessed via a public URL after successful upload.
 */
@RestController
public class UploadController {
    /** The hostname of the target FTP server */
    private static final String FTP_SERVER = "academyws.periskal.com";
    /** The port number for the FTP connection */
    private static final int FTP_PORT = 9910;
    /** Username for FTP authentication */
    private static final String FTP_USER = "AcademyWS";
    /** Password for FTP authentication */
    private static final String FTP_PASSWORD = "Pass4Academy2025!";
    /** Target directory path on the FTP server where files will be stored */
    private static final String TARGET_DIRECTORY = "/groep7/";

    /**
     * Uploads a file to the FTP server.
     * @param file The file to be uploaded, provided as a MultipartFile in the request
     * @return A String containing either the public URL of the uploaded file if successful,
     *         or an error message describing the failure reason
     */
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
            return "https://academyws.periskal.com/Downloads/groep7/" + file.getOriginalFilename();
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