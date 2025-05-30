package ap.student.project.backend.dto;

import ap.student.project.backend.entity.CertificateStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record UserCertificateDTO(
        @JsonProperty("issue_date") LocalDate issueDate,
        @JsonProperty("expiry_date") LocalDate expiryDate,
        @JsonProperty("status") CertificateStatus status,
        @JsonProperty("user_id") int user_id,
        @JsonProperty("certificate_id") int certificate_id
) {
}
