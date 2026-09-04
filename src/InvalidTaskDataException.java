/**
 * Custom domain exception thrown when academic task data fails business validation rules
 * or is malformed during creation or parsing.
 */
public class InvalidTaskDataException extends Exception {

    public InvalidTaskDataException(String message) {
        super(message);
    }

    public InvalidTaskDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
