
package biz.metacode.calcscript.interpreter;

public class ValueMissingException extends ScriptExecutionException {

    private static final long serialVersionUID = -401101866887298035L;

    public ValueMissingException(Throwable cause) {
        super("Operation requires more values.", cause);
    }
}
