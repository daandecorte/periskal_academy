package ap.student.project.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record MessageDTO(
        @JsonProperty("date_time") LocalDateTime dateTime,
        @JsonProperty("text_content") String textContent
) {
}