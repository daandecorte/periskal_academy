package ap.student.project.backend.dto;

import ap.student.project.backend.entity.ProgressStatusType;
import ap.student.project.backend.entity.ModuleProgress;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

public record TrainingProgressDTO(
        @JsonProperty("start_date_time") LocalDateTime startDateTime,
        @JsonProperty("last_time_accessed") LocalDateTime lastTimeAccessed,
        @JsonProperty("status") ProgressStatusType status,
        @JsonProperty("module_progress") List<ModuleProgress> moduleProgresses,
        @JsonProperty("user_training_id") int userTrainingId
) {
}