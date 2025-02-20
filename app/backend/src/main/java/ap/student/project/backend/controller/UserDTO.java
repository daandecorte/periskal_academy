package ap.student.project.backend.controller;

import ap.student.project.backend.entity.Language;
import ap.student.project.backend.entity.Role;
import com.fasterxml.jackson.annotation.JsonProperty;

public record UserDTO(
        @JsonProperty("dongle_id") String dongleId,
        @JsonProperty("fleet_manager_id") int fleetManagerId,
        @JsonProperty("name") String name,
        @JsonProperty("email") String email,
        @JsonProperty("role") Role role,
        @JsonProperty("language") Language language
) {
}
