/**
 * Utility and validator class responsible for validating and normalizing
 * system-level configuration metadata before persistence.
 */
public class SystemValidator {

    // Regex 3: Academic Year Pattern (e.g., 2026-2027)
    public static final String REGEX_ACADEMIC_YEAR = "^\\d{4}-\\d{4}$";

    // Regex 4: Semantic Versioning Pattern (e.g., 1.0.0)
    public static final String REGEX_SEMVER_VERSION = "^\\d+\\.\\d+\\.\\d+$";

    private SystemValidator() {
        // Utility class; prevent instantiation
    }

    /**
     * Cleans, normalizes, and validates an academic year range.
     * <p>
     * Example input: " 2026-2027 " -> Normalized output: "2026-2027"
     *
     * @param raw the raw academic year string
     * @return the normalized academic year string
     * @throws InvalidTaskDataException if format is invalid or ending year is not start year + 1
     */
    public static String normalizeAndValidateAcademicYear(String raw) throws InvalidTaskDataException {
        if (raw == null || raw.isBlank()) {
            throw new InvalidTaskDataException("Academic year cannot be empty or blank.");
        }

        String cleaned = raw.trim();

        if (!cleaned.matches(REGEX_ACADEMIC_YEAR)) {
            throw new InvalidTaskDataException(
                    "Invalid academic year format: '" + raw.trim() + "'. Expected format: YYYY-YYYY (e.g. '2026-2027')."
            );
        }

        String[] years = cleaned.split("-");
        try {
            int startYear = Integer.parseInt(years[0]);
            int endYear = Integer.parseInt(years[1]);

            if (endYear != startYear + 1) {
                throw new InvalidTaskDataException(
                        "Invalid academic year span: '" + cleaned + "'. End year (" + endYear + ") must be exactly one year after start year (" + startYear + ")."
                );
            }
        } catch (NumberFormatException e) {
            throw new InvalidTaskDataException("Academic year contains invalid numbers: '" + cleaned + "'.");
        }

        return cleaned;
    }

    /**
     * Validates an application release version string using semantic versioning regex.
     *
     * @param raw the version string
     * @return cleaned version string
     * @throws InvalidTaskDataException if version does not match semantic versioning
     */
    public static String normalizeAndValidateVersion(String raw) throws InvalidTaskDataException {
        if (raw == null || raw.isBlank()) {
            throw new InvalidTaskDataException("Application version cannot be empty or blank.");
        }

        String cleaned = raw.trim();

        if (!cleaned.matches(REGEX_SEMVER_VERSION)) {
            throw new InvalidTaskDataException(
                    "Invalid version format: '" + raw.trim() + "'. Expected semver format: X.Y.Z (e.g. '1.0.0')."
            );
        }

        return cleaned;
    }

    /**
     * Validates environment identifier (development, staging, production, etc.).
     *
     * @param raw the raw environment string
     * @return normalized lowercase environment string
     * @throws InvalidTaskDataException if blank or contains invalid characters
     */
    public static String normalizeAndValidateEnvironment(String raw) throws InvalidTaskDataException {
        if (raw == null || raw.isBlank()) {
            throw new InvalidTaskDataException("Environment cannot be empty or blank.");
        }

        String cleaned = raw.trim().toLowerCase();

        // Ensure all characters are letters
        for (int i = 0; i < cleaned.length(); i++) {
            char ch = cleaned.charAt(i);
            if (!Character.isLetter(ch)) {
                throw new InvalidTaskDataException(
                        "Environment name must contain letters only (found '" + ch + "' in '" + raw.trim() + "')."
                );
            }
        }

        return cleaned;
    }
}
