package ap.student.project.backend.dto;

import ap.student.project.backend.entity.Question;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ExamDTO(
        @JsonProperty("passing_score") int passingScore,
        @JsonProperty("max_attempts") int maxAttempts,
        @JsonProperty("time") int time,
        @JsonProperty("question_amount") int questionAmount
) {
}