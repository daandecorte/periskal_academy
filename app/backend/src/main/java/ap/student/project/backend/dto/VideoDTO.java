package ap.student.project.backend.dto;

import ap.student.project.backend.entity.Language;
import com.fasterxml.jackson.annotation.JsonProperty;

public record VideoDTO(
        @JsonProperty("video_reference") String videoReference,
        @JsonProperty("language") Language language
) {
}