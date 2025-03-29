package ap.student.project.backend.exceptions;

public class MissingArgumentException extends RuntimeException {
    public MissingArgumentException(String message) {
        super(message);
    }
}
