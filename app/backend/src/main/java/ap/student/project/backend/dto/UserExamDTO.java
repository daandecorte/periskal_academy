package ap.student.project.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record UserExamDTO(
        @JsonProperty("exam_id") int exam_id,
        @JsonProperty("user_training_id") int user_training_id,
        @JsonProperty("exam_attempts") List<ExamAttemptDTO> examAttempts
) {
}