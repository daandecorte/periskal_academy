package ap.student.project.backend.dto;

import ap.student.project.backend.entity.ChatStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ChatDTO(
        @JsonProperty("chat_status") ChatStatus chatStatus
) {
}