package ap.student.project.backend.dto;

import java.util.Optional;

public record LoginRequest(Optional<String> username, Optional<String> password, Optional<String> language, Optional<String> login) {}

