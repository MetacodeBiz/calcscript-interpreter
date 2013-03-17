
package biz.metacode.calcscript.interpreter;

import biz.metacode.calcscript.interpreter.Value.Type;

/**
 * Operator expected value of different type that was given.
 */
public class InvalidTypeException extends ScriptExecutionException {

    private static final long serialVersionUID = 8433423786517792638L;

    private final Type expectedType;

    private final Type actualType;

    /**
     * Constructs new exception with type that was expected and type that was
     * given.
     *
     * @param expectedType Type that was expected.
     * @param actualType Type that was given.
     */
    public InvalidTypeException(final Type expectedType, final Type actualType) {
        super("Expected type " + expectedType + " but got " + actualType + ".");
        this.expectedType = expectedType;
        this.actualType = actualType;
    }

    /**
     * Returns a type that was expected.
     *
     * @return Expected type.
     */
    public Type getExpectedType() {
        return expectedType;
    }

    /**
     * Returns a type that was given.
     *
     * @return Given type.
     */
    public Type getActualType() {
        return actualType;
    }

}
