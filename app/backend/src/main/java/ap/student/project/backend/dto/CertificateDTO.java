package ap.student.project.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CertificateDTO(
        @JsonProperty("training_id") int training_id,
        @JsonProperty("validity_period") int validityPeriod,
        @JsonProperty("price") double price
) {
}
