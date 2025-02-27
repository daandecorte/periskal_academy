package ap.student.project.backend.dto;

import ap.student.project.backend.entity.Question;
import ap.student.project.backend.entity.Training;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

public record TrainingProgressDTO(
        @JsonProperty("training") Training training,
        @JsonProperty("video_watched") boolean videoWatched,
        @JsonProperty("completed_at") LocalDateTime completedAt
) {
}
