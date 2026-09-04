/**
 * Custom domain exception thrown when an academic task with a specific identifier
 * cannot be found in the repository or active query view.
 */
public class TaskNotFoundException extends Exception {

    private final long taskId;

    /**
     * Constructs a TaskNotFoundException with the missing task ID.
     *
     * @param taskId the ID of the task that could not be found
     */
    public TaskNotFoundException(long taskId) {
        super("Task with ID #" + taskId + " was not found.");
        this.taskId = taskId;
    }

    /**
     * Constructs a TaskNotFoundException with a custom message.
     *
     * @param message the detailed exception message
     */
    public TaskNotFoundException(String message) {
        super(message);
        this.taskId = -1;
    }

    /**
     * Constructs a TaskNotFoundException with a custom message and task ID.
     *
     * @param message the detailed exception message
     * @param taskId  the ID of the task that could not be found
     */
    public TaskNotFoundException(String message, long taskId) {
        super(message);
        this.taskId = taskId;
    }

    /**
     * Gets the ID of the task that was not found.
     *
     * @return the task ID, or -1 if unspecified
     */
    public long getTaskId() {
        return taskId;
    }
}
