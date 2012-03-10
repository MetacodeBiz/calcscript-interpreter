
package biz.metacode.calcscript.interpreter;

import biz.metacode.calcscript.interpreter.Value.Type;

/**
 * Operator overload for this type or types is missing.
 */
public class OverloadMissingException extends ScriptExecutionException {

    private static final long serialVersionUID = 6145462421981170L;

    /**
     * Binary operator overload for type {@code left} and {@code right} is
     * missing.
     *
     * @param left First argument's type.
     * @param right Second argument's type.
     */
    public OverloadMissingException(Type left, Type right) {
        super("Overload for types " + left + " and " + right + " is missing.");
    }

    /**
     * Unary operator overload for this type is missing.
     *
     * @param type Argument's type.
     */
    public OverloadMissingException(Type type) {
        super("Overload for type " + type + " is missing.");
    }

}
