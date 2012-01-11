
package biz.metacode.clients.calcscript.interpreter;

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

    Value peek();

    void pushDouble(double value);

    double popDouble();

    void pushString(String value);

    String popString();

    void pushArray(Collection<? extends Value> array);

    SharedArray acquireArray();

    public void markPosition();

    public SharedArray extractMarkedArray();
}
