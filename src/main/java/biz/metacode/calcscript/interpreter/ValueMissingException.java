
package biz.metacode.calcscript.interpreter;

/**
 * Indicates that an operator requires value but it is missing.
 */
public class ValueMissingException extends ScriptExecutionException {

    private static final long serialVersionUID = -401101866887298035L;

    /**
     * Creates new exception with a cause.
     *
     * @param cause Cause of this exception.
     */
    public ValueMissingException(final Throwable cause) {
        super("Operation requires more values.", cause);
    }
}
