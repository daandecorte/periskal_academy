package ap.student.project.backend.controller;

import ap.student.project.backend.entity.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record UserDTO(
        @JsonProperty("dongle_id") String dongleId,
        @JsonProperty("fleet_manager_id") int fleetManagerId,
        @JsonProperty("name") String name,
        @JsonProperty("email") String email,
        @JsonProperty("role") Role role,
        @JsonProperty("language") Language language,
        @JsonProperty("user_exams") List<UserExam> userExams,
        @JsonProperty("user_modules") List<UserModule> userModules
) { }
