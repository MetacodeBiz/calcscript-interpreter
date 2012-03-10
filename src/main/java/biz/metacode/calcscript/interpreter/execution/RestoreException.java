
package biz.metacode.calcscript.interpreter.execution;

/**
 * Indicates an error while restoring memory state.
 */
public class RestoreException extends Exception {

    private static final long serialVersionUID = -3500563913101948969L;

    /**
     * Constructs new exception with given cause.
     *
     * @param cause Cause of an exception.
     */
    public RestoreException(Throwable cause) {
        super(cause);
    }

}
