package ap.student.project.backend.dto;

import ap.student.project.backend.entity.Exam;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record ExamStartResponseDTO(
        Exam exam,
        @JsonProperty("start_time") LocalDateTime startTime
) {
}