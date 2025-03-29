package ap.student.project.backend.dto;

import ap.student.project.backend.entity.Language;
import com.fasterxml.jackson.annotation.JsonProperty;

public record UserDTO(
        @JsonProperty("periskal_id") String periskalId,
        @JsonProperty("firstname") String firstname,
        @JsonProperty("lastname") String lastname,
        @JsonProperty("shipname") String shipname,
        @JsonProperty("language") Language language
) {
}
