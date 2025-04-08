package ap.student.project.backend.dto;

import ap.student.project.backend.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ChatMemberSummaryDTO(
        @JsonProperty("id") int id,
        @JsonProperty("user") User user
) {
}