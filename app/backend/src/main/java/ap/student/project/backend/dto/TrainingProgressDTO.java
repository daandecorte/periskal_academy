package ap.student.project.backend.dto;

import ap.student.project.backend.entity.ProgressStatusType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record TrainingProgressDTO(
        @JsonProperty("start_date_time") LocalDateTime startDateTime,
        @JsonProperty("last_time_accessed") LocalDateTime lastTimeAccessed,
        @JsonProperty("status") ProgressStatusType status,
        @JsonProperty("user_training_id") int userTrainingId
) {
}