package ap.student.project.backend.dto;

import ap.student.project.backend.entity.ProgressStatusType;
import ap.student.project.backend.entity.Question;
import ap.student.project.backend.entity.TrainingProgress;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

public record ModuleProgressDTO(
        @JsonProperty("start_date_time") LocalDateTime startDateTime,
        @JsonProperty("last_time_accessed") LocalDateTime lastTimeAccessed,
        @JsonProperty("status") ProgressStatusType status,
        @JsonProperty("training_progress") List<TrainingProgress> trainingProgress
) {
}