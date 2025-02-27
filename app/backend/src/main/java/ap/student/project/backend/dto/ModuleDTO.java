package ap.student.project.backend.dto;

import ap.student.project.backend.entity.Exam;
import ap.student.project.backend.entity.Language;
import ap.student.project.backend.entity.Question;
import ap.student.project.backend.entity.Training;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ModuleDTO(
        @JsonProperty("title") String title,
        @JsonProperty("description") String description,
        @JsonProperty("language") Language language,
        @JsonProperty("is_active") boolean isActive,
        @JsonProperty("trainings") List<Training> trainings,
        @JsonProperty("exams") List<Exam> exams
) {
}