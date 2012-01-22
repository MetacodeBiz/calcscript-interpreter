
package biz.metacode.calcscript.interpreter.execution;

import biz.metacode.calcscript.interpreter.Value;

class Text extends RefCountedValue implements PooledObject {

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
    public Type getType() {
        return Type.STRING;
    }

    @Override
    public boolean toBoolean() {
        return !"".equals(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Text) {
            return this.value.equals(((Text) obj).value);
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

}
