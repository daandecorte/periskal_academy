package ap.student.project.backend.dto;

import ap.student.project.backend.entity.Language;
import ap.student.project.backend.entity.Video;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record TrainingDTO(
        @JsonProperty("title") Map<Language, String> titles,
        @JsonProperty("description") Map<Language, String> descriptions,
        @JsonProperty("video") Video video
) {
}