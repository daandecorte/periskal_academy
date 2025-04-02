package ap.student.project.backend.dto;

import ap.student.project.backend.entity.Language;
import ap.student.project.backend.entity.Module;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record TipDTO(
        @JsonProperty("text") Map<Language, String> text,
        @JsonProperty("module_id") int moduleId
) {
}