package ap.student.project.backend.dto;

import ap.student.project.backend.entity.Module;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record ModuleProgressDTO(
        @JsonProperty("module") Module module,
        @JsonProperty("video_watched") boolean videoWatched,
        @JsonProperty("completed_at") LocalDateTime completedAt
) {
}
