
package biz.metacode.clients.calcscript.interpreter.execution;

interface PoolProvider {
    <T extends PooledObject> Pool<T> getPool(Class<T> pooledObject);
}
