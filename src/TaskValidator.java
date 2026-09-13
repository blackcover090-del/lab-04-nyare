import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility and service class responsible for sanitizing, normalizing,
 * and validating all academic task data fields before object creation and persistence.
 */
public class TaskValidator {

    // Regex 1: Subject Code Pattern - 2 to 4 letters, space, 3 digits, optional letter suffix (e.g., CCS 201, MATH 019A, GEE 002B)
    public static final String REGEX_SUBJECT_CODE = "^[A-Z]{2,4}\\s\\d{3}[A-Z]?$";

    // Regex 2: Standard Date-Time Pattern - YYYY-MM-DD HH:mm or YYYY-MM-DD HH:mm:ss
    public static final String REGEX_DATETIME = "^\\d{4}-\\d{2}-\\d{2}[ T]\\d{2}:\\d{2}(:\\d{2})?$";

    private static final DateTimeFormatter FORMATTER_MINUTES = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter FORMATTER_SECONDS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private TaskValidator() {
        // Utility class; prevent instantiation
    }

    /**
     * Cleans, normalizes, and validates a course catalog subject code.
     * <p>
     * Example input: "  ccs-201  " -> Normalized output: "CCS 201"
     *
     * @param raw the raw input string
     * @return the normalized subject code
     * @throws InvalidTaskDataException if the input is blank or fails regex structure
     */
    public static String normalizeAndValidateSubjectCode(String raw) throws InvalidTaskDataException {
        if (raw == null || raw.isBlank()) {
            throw new InvalidTaskDataException("Subject code cannot be empty or blank.");
        }

        // String operations: trim leading/trailing whitespace, replace hyphens and multiple spaces with a single space, uppercase
        String normalized = raw.trim().replaceAll("[-_\\s]+", " ").toUpperCase();

        if (!normalized.matches(REGEX_SUBJECT_CODE)) {
            throw new InvalidTaskDataException(
                    "Invalid subject code format: '" + raw.trim() + "'. Expected format: 2-4 uppercase letters followed by a space and 3 digits (e.g. 'CCS 201', 'MATH 019A', 'GEE 002B')."
            );
        }

        return normalized;
    }

    /**
     * Cleans, normalizes, and validates a task title.
     * Enforces length constraints, character-level checks, and capitalizes the first character.
     * <p>
     * Example input: "  polymorphism lab  " -> Normalized output: "Polymorphism lab"
     *
     * @param raw the raw title string
     * @return the normalized task title
     * @throws InvalidTaskDataException if title is blank, invalid length, or does not begin with an alphanumeric character
     */
    public static String normalizeAndValidateTitle(String raw) throws InvalidTaskDataException {
        if (raw == null || raw.isBlank()) {
            throw new InvalidTaskDataException("Task title cannot be empty or blank.");
        }

        // String operations: trim and collapse multiple internal spaces
        String normalized = raw.trim().replaceAll("\\s+", " ");

        if (normalized.length() < 3 || normalized.length() > 60) {
            throw new InvalidTaskDataException(
                    "Task title length must be between 3 and 60 characters (received " + normalized.length() + " chars: \"" + normalized + "\")."
            );
        }

        // Character method check: first character must be a letter or digit
        char firstChar = normalized.charAt(0);
        if (!Character.isLetterOrDigit(firstChar)) {
            throw new InvalidTaskDataException(
                    "Task title must start with a letter or digit. Found invalid leading character: '" + firstChar + "'."
            );
        }

        // Normalize first character to uppercase
        return Character.toUpperCase(firstChar) + (normalized.length() > 1 ? normalized.substring(1) : "");
    }

    /**
     * Cleans and validates detailed notes or instructions for a task.
     * Notes can be empty/optional, but if provided, excess whitespace is trimmed.
     *
     * @param raw the raw notes string
     * @return normalized notes string
     */
    public static String normalizeNotes(String raw) {
        if (raw == null || raw.isBlank()) {
            return "";
        }
        return raw.trim().replaceAll("\\s+", " ");
    }

    /**
     * Safely validates and parses a numeric identifier (e.g. task ID, subject ID).
     *
     * @param raw       the raw numeric string
     * @param fieldName the user-friendly field name for diagnostic messages
     * @return the parsed positive long value
     * @throws InvalidTaskDataException if string is blank, contains non-digits, or is <= 0
     */
    public static long parseAndValidateId(String raw, String fieldName) throws InvalidTaskDataException {
        if (raw == null || raw.isBlank()) {
            throw new InvalidTaskDataException(fieldName + " cannot be empty or blank.");
        }

        String cleaned = raw.trim();

        // Character-level inspection: verify every character is a digit
        for (int i = 0; i < cleaned.length(); i++) {
            char ch = cleaned.charAt(i);
            if (!Character.isDigit(ch)) {
                throw new InvalidTaskDataException(
                        fieldName + " must contain digits only. Found invalid character '" + ch + "' at index " + (i + 1) + " in input '" + cleaned + "'."
                );
            }
        }

        try {
            long val = Long.parseLong(cleaned);
            if (val <= 0) {
                throw new InvalidTaskDataException(fieldName + " must be a positive number greater than 0 (received: " + val + ").");
            }
            return val;
        } catch (NumberFormatException e) {
            throw new InvalidTaskDataException(fieldName + " value '" + raw + "' exceeds valid numeric range.");
        }
    }

    /**
     * Cleans, validates, and parses a date-time string into a LocalDateTime instance.
     * <p>
     * Accepts: "YYYY-MM-DD HH:mm", "YYYY-MM-DD HH:mm:ss", "YYYY-MM-DDTHH:mm", "YYYY-MM-DDTHH:mm:ss"
     *
     * @param raw the raw date-time string
     * @return the parsed LocalDateTime object
     * @throws InvalidTaskDataException if string does not match date-time regex or is not a valid calendar date
     */
    public static LocalDateTime parseAndValidateDateTime(String raw) throws InvalidTaskDataException {
        if (raw == null || raw.isBlank()) {
            throw new InvalidTaskDataException("Due date cannot be empty or blank.");
        }

        String cleaned = raw.trim().replace("T", " ");

        if (!cleaned.matches(REGEX_DATETIME)) {
            throw new InvalidTaskDataException(
                    "Invalid due date format: '" + raw.trim() + "'. Expected format: YYYY-MM-DD HH:mm (e.g., '2026-08-25 14:30')."
            );
        }

        try {
            if (cleaned.length() == 16) {
                return LocalDateTime.parse(cleaned, FORMATTER_MINUTES);
            } else {
                return LocalDateTime.parse(cleaned, FORMATTER_SECONDS);
            }
        } catch (DateTimeParseException e) {
            throw new InvalidTaskDataException(
                    "Invalid calendar date/time value: '" + raw.trim() + "'. Please check month (01-12), day (01-31), hour (00-23), and minute (00-59)."
            );
        }
    }

    /**
     * Normalizes and validates the task type from a string representation.
     *
     * @param raw the raw task type string
     * @return the matching TaskType enum
     * @throws InvalidTaskDataException if raw string does not match any known TaskType
     */
    public static TaskType parseAndValidateTaskType(String raw) throws InvalidTaskDataException {
        if (raw == null || raw.isBlank()) {
            throw new InvalidTaskDataException("Task type cannot be empty or blank.");
        }

        String cleaned = raw.trim();

        for (TaskType type : TaskType.values()) {
            // String comparison using equalsIgnoreCase
            if (type.name().equalsIgnoreCase(cleaned)) {
                return type;
            }
        }

        StringBuilder validTypes = new StringBuilder();
        for (TaskType type : TaskType.values()) {
            if (!validTypes.isEmpty()) {
                validTypes.append(", ");
            }
            validTypes.append(type.name());
        }

        throw new InvalidTaskDataException(
                "Invalid task type: '" + raw.trim() + "'. Supported types: " + validTypes + "."
        );
    }

    /**
     * Normalizes and validates the task status from a string representation.
     *
     * @param raw the raw task status string
     * @return the matching TaskStatus enum
     * @throws InvalidTaskDataException if raw string does not match any known TaskStatus
     */
    public static TaskStatus parseAndValidateTaskStatus(String raw) throws InvalidTaskDataException {
        if (raw == null || raw.isBlank()) {
            return TaskStatus.PENDING; // Default status if omitted
        }

        String cleaned = raw.trim();

        for (TaskStatus status : TaskStatus.values()) {
            if (status.name().equalsIgnoreCase(cleaned)) {
                return status;
            }
        }

        throw new InvalidTaskDataException(
                "Invalid task status: '" + raw.trim() + "'. Supported statuses: PENDING, COMPLETED, POSTPONED."
        );
    }
}
