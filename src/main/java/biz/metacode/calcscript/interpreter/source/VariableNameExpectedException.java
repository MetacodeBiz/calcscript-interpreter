
package biz.metacode.calcscript.interpreter.source;

public class VariableNameExpectedException extends SyntaxException {

    private static final long serialVersionUID = -7632991842606732184L;

    public VariableNameExpectedException() {
        super("Expected variable name>");
    }
}
