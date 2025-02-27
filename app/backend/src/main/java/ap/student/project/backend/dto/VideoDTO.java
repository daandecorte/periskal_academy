package ap.student.project.backend.dto;

import ap.student.project.backend.entity.Question;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record VideoDTO(
        @JsonProperty("video_reference") String videoReference
) {
}