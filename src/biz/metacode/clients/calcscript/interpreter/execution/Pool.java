
package biz.metacode.clients.calcscript.interpreter.execution;

public interface Pool<T extends PooledObject<T>> {
    T acquire();

    void relinquish(T object);
}
