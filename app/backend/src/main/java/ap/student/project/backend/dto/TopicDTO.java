package ap.student.project.backend.dto;

import ap.student.project.backend.entity.Language;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public record TopicDTO(
        @JsonProperty("title") Map<Language, String> title
        ) {
}
