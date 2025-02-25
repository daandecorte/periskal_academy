package ap.student.project.backend.dto;

import ap.student.project.backend.entity.Language;
import ap.student.project.backend.entity.Question;
import ap.student.project.backend.entity.Video;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TrainingDTO(
        @JsonProperty("title") String title,
        @JsonProperty("description") String description,
        @JsonProperty("language") Language language,
        @JsonProperty("video") Video video
) {
}