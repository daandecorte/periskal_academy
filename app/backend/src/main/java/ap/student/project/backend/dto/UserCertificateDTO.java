package ap.student.project.backend.dto;

import ap.student.project.backend.entity.Certificate;
import ap.student.project.backend.entity.CertificateStatus;
import ap.student.project.backend.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record UserCertificateDTO(
        @JsonProperty("issue_date") LocalDateTime issueDate,
        @JsonProperty("expiry_date") LocalDateTime expiryDate,
        @JsonProperty("status") CertificateStatus status,
        @JsonProperty("user_id") int user_id,
        @JsonProperty("certificate_id") int certificate_id
        ) {
}
