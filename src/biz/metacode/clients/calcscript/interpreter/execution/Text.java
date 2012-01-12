
package biz.metacode.clients.calcscript.interpreter.execution;

import biz.metacode.clients.calcscript.interpreter.Value;

public class Text extends Value implements PooledObject<Text> {

    private static final long serialVersionUID = 1168274352878421240L;

    private String value;

    private transient Pool<Text> pool;

    Text(Pool<Text> pool, String value) {
        this.pool = pool;
        this.value = value;
    }

    public String consume() {
        String value = this.value;
        this.pool.relinquish(this);
        return value;
    }

    void set(String string) {
        this.value = string;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    protected int getPriority() {
        return 3;
    }

    @Override
    public Value duplicate() {
        Text duplicate = this.pool.acquire();
        duplicate.set(value);
        return duplicate;
    }

    public void attachToPool(Pool<Text> pool) {
        this.pool = pool;
    }
}
