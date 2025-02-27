package ap.student.project.backend.dto;

import ap.student.project.backend.entity.CertificateStatus;
import ap.student.project.backend.entity.Module;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record CertificateDTO(
        @JsonProperty("module") Module module,
        @JsonProperty("issue_date") LocalDateTime issueDate,
        @JsonProperty("expiry_date") LocalDateTime expiryDate,
        @JsonProperty("validity_period") int validityPeriod,
        @JsonProperty("status") CertificateStatus status,
        @JsonProperty("price") double price
) {
}
