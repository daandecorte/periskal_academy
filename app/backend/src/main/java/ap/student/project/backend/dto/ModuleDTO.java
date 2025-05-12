package ap.student.project.backend.dto;

import ap.student.project.backend.entity.Content;
import ap.student.project.backend.entity.Language;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public record ModuleDTO(
        @JsonProperty("title") Map<Language, String> title,
        @JsonProperty("description") Map<Language, String> description,
        @JsonProperty("content") List<Content> content,
        @JsonProperty("training_id") int trainingId
) {
}