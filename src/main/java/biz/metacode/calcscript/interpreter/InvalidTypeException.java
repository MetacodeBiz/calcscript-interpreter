
package biz.metacode.calcscript.interpreter;

import biz.metacode.calcscript.interpreter.Value.Type;

public class InvalidTypeException extends ScriptExecutionException {

    private static final long serialVersionUID = 8433423786517792638L;

    private final Type expectedType;

    private final Type actualType;

    public InvalidTypeException(Type expectedType, Type actualType) {
        super("Expected type " + expectedType + " but got " + actualType + ".");
        this.expectedType = expectedType;
        this.actualType = actualType;
    }

    public Type getExpectedType() {
        return expectedType;
    }

    public Type getActualType() {
        return actualType;
    }

}
