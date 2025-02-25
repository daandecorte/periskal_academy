package ap.student.project.backend.dto;

import ap.student.project.backend.entity.Language;
import ap.student.project.backend.entity.QuestionOption;
import ap.student.project.backend.entity.QuestionType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record QuestionDTO(
        @JsonProperty("language") Language language,
        @JsonProperty("text") String text,
        @JsonProperty("question_type") QuestionType questionType,
        @JsonProperty("question_options") List<QuestionOption> questionOptions
) {
}