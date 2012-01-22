
package biz.metacode.calcscript.interpreter.execution;

interface Pool<T extends PooledObject> {
    T create();

    void destroy(T object);

    void setTrait(String trait);
}
