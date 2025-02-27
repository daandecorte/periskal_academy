package ap.student.project.backend.dto;

import ap.student.project.backend.entity.ExamStatusType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record ExamAttemptDTO(
        @JsonProperty("start_date_time") LocalDateTime startDateTime,
        @JsonProperty("end_date_time") LocalDateTime endDateTime,
        @JsonProperty("exam_status_type") ExamStatusType examStatusType,
        @JsonProperty("score") int score
) {
}