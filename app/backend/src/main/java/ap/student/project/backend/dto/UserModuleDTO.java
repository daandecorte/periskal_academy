package ap.student.project.backend.dto;

import ap.student.project.backend.entity.ModuleProgress;
import com.fasterxml.jackson.annotation.JsonProperty;

public record UserModuleDTO(
        @JsonProperty("module_progress") ModuleProgress moduleProgress,
        @JsonProperty("module_id") int module_id,
        @JsonProperty("user_id") int user_id
) {
}