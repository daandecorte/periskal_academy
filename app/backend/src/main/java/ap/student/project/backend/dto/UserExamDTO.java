package ap.student.project.backend.dto;

import ap.student.project.backend.entity.Question;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record UserExamDTO(
        @JsonProperty("exam_id") int exam_id,
        @JsonProperty("user_id") int user_id,
        @JsonProperty("exam_attempts") List<ExamAttemptDTO> examAttempts
) {
}