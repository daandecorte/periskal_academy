package ap.student.project.backend.dto;

import ap.student.project.backend.entity.TrainingProgress;
import com.fasterxml.jackson.annotation.JsonProperty;

public record UserTrainingDTO(
        @JsonProperty("training_id") int training_id,
        @JsonProperty("user_id") int user_id,
        @JsonProperty("eligible_for_certificate") boolean eligibleForCertificate
) {
}