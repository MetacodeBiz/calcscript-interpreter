
package biz.metacode.clients.calcscript.interpreter.execution;

public interface Pool<T> {
    T acquire();

    void relinquish(T object);
}
