
package biz.metacode.calcscript.interpreter.source;

/**
 * Thrown when an unclosed block is encountered in script.
 */
public class UnclosedBlockException extends SyntaxException {

    private static final long serialVersionUID = 6293253614117453910L;

    /**
     * Constructs new exception.
     */
    public UnclosedBlockException() {
        super("Missing } sign.");
    }

}
