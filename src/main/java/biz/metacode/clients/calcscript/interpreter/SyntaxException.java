
package biz.metacode.clients.calcscript.interpreter;

/**
 * Represents an error during parsing.
 */
public class SyntaxException extends RuntimeException {

    private static final long serialVersionUID = -1536559671465479410L;

    public SyntaxException(String message) {
        super(message);
    }

}