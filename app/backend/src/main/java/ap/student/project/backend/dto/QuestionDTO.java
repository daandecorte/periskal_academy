package ap.student.project.backend.dto;

import ap.student.project.backend.entity.Language;
import ap.student.project.backend.entity.QuestionOption;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public record QuestionDTO(
        @JsonProperty("text") Map<Language, String> text,
        @JsonProperty("question_options") List<QuestionOption> questionOptions
) {
}