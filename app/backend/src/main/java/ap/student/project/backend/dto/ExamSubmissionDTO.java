package ap.student.project.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ExamSubmissionDTO(
        @JsonProperty("examId") int examId,
        @JsonProperty("userId") int userId,
        @JsonProperty("answers") List<ExamAnswerDTO> answers
) {
}