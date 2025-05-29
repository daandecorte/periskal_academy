package ap.student.project.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExamAnswerDTO(
        @JsonProperty("questionId") int questionId,
        @JsonProperty("optionId") int optionId
) {
}