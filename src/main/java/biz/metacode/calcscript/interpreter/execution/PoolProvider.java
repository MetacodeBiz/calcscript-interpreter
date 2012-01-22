
package biz.metacode.calcscript.interpreter.execution;

interface PoolProvider {
    <T extends PooledObject> Pool<T> getPool(Class<T> pooledObject);
}
