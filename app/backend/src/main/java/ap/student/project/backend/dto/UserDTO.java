package ap.student.project.backend.dto;

import ap.student.project.backend.entity.Language;
import ap.student.project.backend.entity.Role;
import ap.student.project.backend.entity.UserExam;
import ap.student.project.backend.entity.UserModule;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record UserDTO(
        @JsonProperty("user_id") String userId,
        @JsonProperty("language") Language language
) {
}
