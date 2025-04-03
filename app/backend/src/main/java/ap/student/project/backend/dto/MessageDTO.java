package ap.student.project.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record MessageDTO(
        @JsonProperty("date_time") LocalDateTime dateTime,
        @JsonProperty("text_content") String textContent,
        @JsonProperty("chat_member_id") int chat_member_id,
        @JsonProperty("chat_id") int chat_id
) {
}