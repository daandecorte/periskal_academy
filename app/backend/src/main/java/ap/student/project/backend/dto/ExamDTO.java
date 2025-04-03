package ap.student.project.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExamDTO(
        @JsonProperty("passing_score") int passingScore,
        @JsonProperty("max_attempts") int maxAttempts,
        @JsonProperty("time") int time,
        @JsonProperty("question_amount") int questionAmount,
        @JsonProperty("training_id") int trainingId
) {
}