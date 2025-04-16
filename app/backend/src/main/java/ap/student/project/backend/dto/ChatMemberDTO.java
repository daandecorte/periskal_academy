package ap.student.project.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ChatMemberDTO(
        @JsonProperty("user_id") int user_id,
        @JsonProperty("chat_id") int chat_id
) {
}
