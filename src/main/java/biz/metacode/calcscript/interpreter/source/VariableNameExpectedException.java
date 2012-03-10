
package biz.metacode.calcscript.interpreter.source;

/**
 * Indicates that the assignment in script is missing a valid variable name.
 */
public class VariableNameExpectedException extends SyntaxException {

    private static final long serialVersionUID = -7632991842606732184L;

    /**
     * Constructs new exception.
     */
    public VariableNameExpectedException() {
        super("Expected variable name.");
    }
}
