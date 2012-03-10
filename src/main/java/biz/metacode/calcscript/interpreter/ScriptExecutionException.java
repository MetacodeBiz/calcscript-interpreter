
package biz.metacode.calcscript.interpreter;

/**
 * Error during script execution.
 */
public class ScriptExecutionException extends RuntimeException {

    private static final long serialVersionUID = -2441264131952720908L;

    private String operatorName;

    private String example;

    /**
     * Constructs new exception with given message and error's cause.
     *
     * @param message Error message.
     * @param cause Error cause.
     */
    public ScriptExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs new exception with given message.
     *
     * @param message Error message.
     */
    public ScriptExecutionException(String message) {
        super(message);
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
     * Sets last executed operator's name. Used internally by execution engine
     * to track which operator was last executed before encountering the error.
     *
     * @param operatorName Name of the last operator.
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    /**
     * Gets an example of correct usage for operator {@link #getOperatorName()}.
     *
     * @return Example usage or {@code null} if it's not available.
     */
    public String getExample() {
        return example;
    }

    /**
     * Sets an example of correct usage for operator {@link #getOperatorName()}.
     *
     * @param example Example usage for operator.
     */
    public void setExample(String example) {
        this.example = example;
    }
}
