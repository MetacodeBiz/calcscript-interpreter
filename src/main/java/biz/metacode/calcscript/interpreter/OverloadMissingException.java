
package biz.metacode.calcscript.interpreter;

import biz.metacode.calcscript.interpreter.Value.Type;

public class OverloadMissingException extends ScriptExecutionException {

    private static final long serialVersionUID = 6145462421981170L;

    public OverloadMissingException(Type left, Type right) {
        super("Overload for types " + left + " and " + right + " is missing.");
    }

    public OverloadMissingException(Type type) {
        super("Overload for type " + type + " is missing.");
    }

}
