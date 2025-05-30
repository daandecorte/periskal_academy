package ap.student.project.backend.dto;

import ap.student.project.backend.entity.ChatStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ChatSummaryDTO(
        @JsonProperty("id") int id,
        @JsonProperty("chat_members") List<ChatMemberSummaryDTO> chatMembers,
        @JsonProperty("chat_status") ChatStatus chatStatus
) {
}

