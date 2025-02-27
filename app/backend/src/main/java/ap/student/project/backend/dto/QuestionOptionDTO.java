package ap.student.project.backend.dto;

import ap.student.project.backend.entity.Question;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record QuestionOptionDTO(
        @JsonProperty("text") String text,
        @JsonProperty("is_correct") boolean isCorrect
) {
}