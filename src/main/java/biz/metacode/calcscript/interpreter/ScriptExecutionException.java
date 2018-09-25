
package biz.metacode.calcscript.interpreter;

/**
 * Error during script execution.
 */
public class ScriptExecutionException extends RuntimeException {

    private static final long serialVersionUID = -2441264131952720908L;

    private final String operatorName;

    private final String example;

    /**
     * Constructs new exception with given message and error's cause.
     *
     * @param message Error message.
     * @param cause Error cause.
     */
    public ScriptExecutionException(final String message, final Throwable cause) {
        super(message, cause);
        this.operatorName = null;
        this.example = null;
    }

    /**
     * Constructs new exception with given operator name and example usage..
     *
     * @param operatorName Operator that caused the reror.
     * @param example Example, correct usage.
     * @param cause Error cause.
     */
    public ScriptExecutionException(final String operatorName, final String example, final Throwable cause) {
        super("Script execution failed for operator: " + operatorName, cause);
        this.operatorName = operatorName;
        this.example = example;
    }

    /**
     * Constructs new exception with given message.
     *
     * @param message Error message.
     */
    public ScriptExecutionException(final String message) {
        this(message, null);
    }

    /**
     * Gets last executed operator's name. May be {@code null}.
     *
     * @return Operator name or {@code null} if the name is not available.
     */
    public String getOperatorName() {
        return operatorName;
    }

    /**
     * Gets an example of correct usage for operator {@link #getOperatorName()}.
     *
     * @return Example usage or {@code null} if it's not available.
     */
    public String getExample() {
        return example;
    }
}
