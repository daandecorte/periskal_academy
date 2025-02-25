package ap.student.project.backend.dto;

import ap.student.project.backend.entity.Question;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record UserExamDTO(
        @JsonProperty("exam") ExamDTO exam,
        @JsonProperty("exam_attempts") List<ExamAttemptDTO> examAttempts
) {
}