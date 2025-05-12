package ap.student.project.backend.dto;

import ap.student.project.backend.entity.ContentType;
import ap.student.project.backend.entity.Language;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record ContentDTO(
        @JsonProperty("content_type") ContentType contentType,
        @JsonProperty("reference") Map<Language, String> reference
) {
}
