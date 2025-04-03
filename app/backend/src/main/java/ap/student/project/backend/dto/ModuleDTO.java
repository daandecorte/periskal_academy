package ap.student.project.backend.dto;

import ap.student.project.backend.entity.*;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public record ModuleDTO(
        @JsonProperty("title") Map<Language, String> title,
        @JsonProperty("description") Map<Language, String> description,
        @JsonProperty("is_active") boolean isActive,
        @JsonProperty("trainings") List<Training> trainings,
        @JsonProperty("exams") List<Exam> exams
) {
}