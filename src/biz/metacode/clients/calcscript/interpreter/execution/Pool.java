
package biz.metacode.clients.calcscript.interpreter.execution;

public interface Pool<T> {
    void relinquish(T object);
}
