
package biz.metacode.clients.calcscript.interpreter.execution;

public interface Pool<T extends PooledObject> {
    T create();

    void destroy(T object);
}
