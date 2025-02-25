package ap.student.project.backend.dto;

import ap.student.project.backend.entity.Language;
import ap.student.project.backend.entity.Role;
import ap.student.project.backend.entity.UserExam;
import ap.student.project.backend.entity.UserModule;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record UserDTO(
        @JsonProperty("dongle_id") String dongleId,
        @JsonProperty("fleet_manager_id") int fleetManagerId,
        @JsonProperty("name") String name,
        @JsonProperty("email") String email,
        @JsonProperty("role") Role role,
        @JsonProperty("language") Language language,
        @JsonProperty("user_exams") List<UserExam> userExams,
        @JsonProperty("user_modules") List<UserModule> userModules
) {
}
