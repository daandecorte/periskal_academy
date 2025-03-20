package ap.student.project.backend.dto;

import ap.student.project.backend.entity.Module;
import com.fasterxml.jackson.annotation.JsonProperty;

public record CertificateDTO(
        @JsonProperty("module") Module module,
        @JsonProperty("validity_period") int validityPeriod,
        @JsonProperty("price") double price
) {
}
