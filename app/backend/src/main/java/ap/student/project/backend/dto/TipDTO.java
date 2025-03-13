package ap.student.project.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TipDTO(
        @JsonProperty("text") String text,
        @JsonProperty("module") Module module
) {
}