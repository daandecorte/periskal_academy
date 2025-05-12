package ap.student.project.backend.dto;

import ap.student.project.backend.entity.*;
import ap.student.project.backend.entity.Module;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public record TrainingDTO(
        @JsonProperty("title") Map<Language, String> title,
        @JsonProperty("description") Map<Language, String> description,
        @JsonProperty("is_active") boolean isActive,
        @JsonProperty("modules") List<Module> modules,
        @JsonProperty("exam") Exam exams
) {
}