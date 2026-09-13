import java.time.LocalDateTime;

/**
 * Represents an academic task in the study planner system.
 * This is a data model class that holds information about subject IDs, subject codes,
 * task titles, detailed notes, task types, due dates, and completion status.
 */
public class AcademicTask {

    private long id;
    private long subjectId;
    private String subjectCode;
    private String title;
    private String notes;
    private TaskType type;
    private LocalDateTime dueDate;
    private TaskStatus status;

    /**
     * Default constructor for AcademicTask.
     */
    public AcademicTask() {
    }

    /**
     * Constructs a new AcademicTask with the specified properties.
     *
     * @param id          the unique identifier of the task
     * @param subjectId   the database ID of the associated course
     * @param subjectCode the catalog code of the course (e.g. CCS 201)
     * @param title       the brief description/header of the task
     * @param notes       the detailed instruction text, class diaries, or notes
     * @param type        the type of task (e.g. Activity, Exam)
     * @param dueDate     the deadline timestamp for completion
     * @param status      the status indicating PENDING or COMPLETED
     */
    public AcademicTask(
            long id,
            long subjectId,
            String subjectCode,
            String title,
            String notes,
            TaskType type,
            LocalDateTime dueDate,
            TaskStatus status) {

        this.id = id;
        this.subjectId = subjectId;
        this.subjectCode = subjectCode;
        this.title = title;
        this.notes = notes;
        this.type = type;
        this.dueDate = dueDate;
        this.status = status;
    }

    /**
     * Factory method that cleans, normalizes, and validates all input data
     * before constructing an AcademicTask instance.
     *
     * @param rawId          raw task ID string or number
     * @param rawSubjectId   raw subject ID string or number
     * @param rawSubjectCode raw course code string (e.g. "  ccs-201 ")
     * @param rawTitle       raw task title string (e.g. "  polymorphism lab ")
     * @param rawNotes       raw notes string
     * @param rawType        raw task type string (e.g. "activity")
     * @param rawDueDate     raw due date string (e.g. "2026-08-25 14:00")
     * @param rawStatus      raw status string (e.g. "pending")
     * @return a normalized and validated AcademicTask instance
     * @throws InvalidTaskDataException if any field is invalid
     */
    public static AcademicTask createFromRaw(
            String rawId,
            String rawSubjectId,
            String rawSubjectCode,
            String rawTitle,
            String rawNotes,
            String rawType,
            String rawDueDate,
            String rawStatus) throws InvalidTaskDataException {

        long id = TaskValidator.parseAndValidateId(rawId, "Task ID");
        long subjectId = TaskValidator.parseAndValidateId(rawSubjectId, "Subject ID");
        String subjectCode = TaskValidator.normalizeAndValidateSubjectCode(rawSubjectCode);
        String title = TaskValidator.normalizeAndValidateTitle(rawTitle);
        String notes = TaskValidator.normalizeNotes(rawNotes);
        TaskType type = TaskValidator.parseAndValidateTaskType(rawType);
        LocalDateTime dueDate = TaskValidator.parseAndValidateDateTime(rawDueDate);
        TaskStatus status = TaskValidator.parseAndValidateTaskStatus(rawStatus);

        AcademicTask task = new AcademicTask(id, subjectId, subjectCode, title, notes, type, dueDate, status);
        task.validate();
        return task;
    }

    /**
     * Gets the unique identifier of this academic task.
     *
     * @return the task ID
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the unique identifier of this academic task.
     *
     * @param id the task ID to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the ID of the course associated with this task.
     *
     * @return the subject database ID
     */
    public long getSubjectId() {
        return subjectId;
    }

    /**
     * Sets the ID of the course associated with this task.
     *
     * @param subjectId the subject database ID to set
     */
    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }

    /**
     * Gets the course catalog code of this task.
     *
     * @return the subject code string
     */
    public String getSubjectCode() {
        return subjectCode;
    }

    /**
     * Sets the course catalog code of this task.
     *
     * @param subjectCode the subject code string to set
     */
    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    /**
     * Gets the title of this academic task.
     *
     * @return the task title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of this academic task.
     *
     * @param title the task title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the detailed notes, directions, or diary entry of this task.
     *
     * @return the task notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the detailed notes, directions, or diary entry of this task.
     *
     * @param notes the task notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Gets the type category of this task.
     *
     * @return the task type enum
     */
    public TaskType getType() {
        return type;
    }

    /**
     * Sets the type category of this task.
     *
     * @param type the task type enum to set
     */
    public void setType(TaskType type) {
        this.type = type;
    }

    /**
     * Gets the deadline timestamp for this task.
     *
     * @return the due date and time
     */
    public LocalDateTime getDueDate() {
        return dueDate;
    }

    /**
     * Sets the deadline timestamp for this task.
     *
     * @param dueDate the due date and time to set
     */
    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Gets the current completion status of this task.
     *
     * @return the task status enum
     */
    public TaskStatus getStatus() {
        return status;
    }

    /**
     * Sets the current completion status of this task.
     *
     * @param status the task status enum to set
     */
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    /**
     * Validates that this academic task conforms to all domain rules and formatting constraints.
     *
     * @throws InvalidTaskDataException if any domain field is missing or invalid
     */
    public void validate() throws InvalidTaskDataException {
        if (id <= 0) {
            throw new InvalidTaskDataException("Task ID must be greater than 0 (received: " + id + ").");
        }
        if (subjectId <= 0) {
            throw new InvalidTaskDataException("Subject ID must be greater than 0 (received: " + subjectId + ").");
        }
        if (subjectCode == null || subjectCode.isBlank()) {
            throw new InvalidTaskDataException("Subject code cannot be null or blank.");
        }
        // Normalize and re-verify format
        this.subjectCode = TaskValidator.normalizeAndValidateSubjectCode(this.subjectCode);

        if (title == null || title.isBlank()) {
            throw new InvalidTaskDataException("Task title cannot be null or blank.");
        }
        this.title = TaskValidator.normalizeAndValidateTitle(this.title);

        if (notes != null) {
            this.notes = TaskValidator.normalizeNotes(this.notes);
        } else {
            this.notes = "";
        }

        if (type == null) {
            throw new InvalidTaskDataException("Task type cannot be null.");
        }
        if (dueDate == null) {
            throw new InvalidTaskDataException("Task due date cannot be null.");
        }
        if (status == null) {
            throw new InvalidTaskDataException("Task status cannot be null.");
        }
    }
}