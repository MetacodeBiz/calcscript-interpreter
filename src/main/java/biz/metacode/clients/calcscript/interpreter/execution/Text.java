
package biz.metacode.clients.calcscript.interpreter.execution;

import biz.metacode.clients.calcscript.interpreter.Value;

public class Text extends RefCountedValue implements PooledObject {

    private static final long serialVersionUID = 1168274352878421240L;

    private String value;

    private transient Pool<Text> pool;

    Text(Pool<Text> pool, String value) {
        this.pool = pool;
        this.value = value;
    }

    public String get() {
        return value;
    }

    void set(String string) {
        if (isShared()) {
            throw new IllegalStateException("Object is shared.");
        }
        this.value = string;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int getPriority() {
        return 3;
    }

    @Override
    public Value duplicate() {
        Text duplicate = this.pool.create();
        duplicate.set(value);
        return duplicate;
    }

    public void attachToPool(PoolProvider poolProvider) {
        this.pool = poolProvider.getPool(Text.class);
    }

    @Override
    protected void relinquish() {
        this.pool.destroy(this);
    }

    @Override
    public String getTypeName() {
        return "string";
    }

    @Override
    public boolean toBoolean() {
        return !"".equals(value);
    }
}
