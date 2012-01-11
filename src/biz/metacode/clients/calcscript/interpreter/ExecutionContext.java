
package biz.metacode.clients.calcscript.interpreter;

import java.util.Collection;

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
