
package biz.metacode.clients.calcscript.interpreter.execution;

public interface PoolProvider {
    <T extends PooledObject> Pool<T> getPool(Class<T> pooledObject);
}
