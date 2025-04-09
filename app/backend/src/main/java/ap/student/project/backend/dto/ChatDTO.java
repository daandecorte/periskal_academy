package ap.student.project.backend.dto;

import ap.student.project.backend.entity.ChatMember;
import ap.student.project.backend.entity.ChatStatus;
import ap.student.project.backend.entity.Message;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public record ChatDTO(
        @JsonProperty("chat_status") ChatStatus chatStatus
) {
}