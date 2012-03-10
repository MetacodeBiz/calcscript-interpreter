
package biz.metacode.calcscript.interpreter.source;

/**
 * Represents an error during parsing.
 */
public class SyntaxException extends RuntimeException {

    private static final long serialVersionUID = -1536559671465479410L;

    /**
     * Constructs new exception with given message.
     *
     * @param message Error message.
     */
    public SyntaxException(String message) {
        super(message);
    }

}
