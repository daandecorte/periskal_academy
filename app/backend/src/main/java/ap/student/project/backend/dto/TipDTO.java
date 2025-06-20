package ap.student.project.backend.dto;

import ap.student.project.backend.entity.Language;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record TipDTO(
        @JsonProperty("topic_id") int topic_id,
        @JsonProperty("text") Map<Language, String> text
) {
}