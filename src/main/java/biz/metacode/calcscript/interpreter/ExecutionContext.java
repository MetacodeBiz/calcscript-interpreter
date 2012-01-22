
package biz.metacode.calcscript.interpreter;

import biz.metacode.calcscript.interpreter.Value.Pair;

import java.util.Collection;

/**
 * Context of program execution. Represents state of memory (read/write) and
 * stack (push/pop).
 */
public interface ExecutionContext {

    void write(String name, Invocable object);

    Invocable read(String name);

    void push(Value value);

    Value pop();

    Value peekNth(int n);

    Value peek();

    void pushDouble(double value);

    double popDouble();

    void pushString(String value);

    String popString();

    void pushBoolean(boolean value);

    boolean popBoolean();

    void pushArray(Collection<? extends Value> array);

    SharedArray acquireArray();

    Value convertToValue(Collection<? extends Value> array);

    Value convertToValue(String string);

    Value convertToValue(double value);

    Pair coerce(Value first, Value second);

    public void markPosition();

    public SharedArray extractMarkedArray();

    void interruptionPoint() throws InterruptedException;
}
