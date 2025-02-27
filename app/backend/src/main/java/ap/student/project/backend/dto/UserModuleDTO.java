package ap.student.project.backend.dto;

import ap.student.project.backend.entity.ModuleProgress;
import com.fasterxml.jackson.annotation.JsonProperty;

public record UserModuleDTO(
        @JsonProperty("module_progress") ModuleProgress moduleProgress,
        @JsonProperty("module") ModuleDTO module
) {
}