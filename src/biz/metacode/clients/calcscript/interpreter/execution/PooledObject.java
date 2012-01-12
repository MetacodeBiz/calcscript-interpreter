
package biz.metacode.clients.calcscript.interpreter.execution;

public interface PooledObject<T extends PooledObject<T>> {

    void attachToPool(Pool<T> pool);

}
