
package biz.metacode.calcscript.interpreter.execution;

import biz.metacode.calcscript.interpreter.Value;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

class Numeric extends RefCountedValue implements PooledObject {

    private static final long serialVersionUID = -7340834444775795549L;

    private static final DecimalFormat THREE_DECIMAL_PLACES;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        THREE_DECIMAL_PLACES = new DecimalFormat("0.###", symbols);
    }

    private transient Pool<Numeric> pool;

    private double value;

    Numeric(final Pool<Numeric> pool, final double value) {
        this.pool = pool;
        this.value = value;
    }

    public double get() {
        return value;
    }

    void set(final double newValue) {
        if (isShared()) {
            throw new IllegalStateException("Object is shared.");
        }
        this.value = newValue;
    }

    @Override
    public String toString() {
        return THREE_DECIMAL_PLACES.format(value);
    }

    @Override
    public int getPriority() {
        return Value.PRIORITY_LOW;
    }

    @Override
    public int compareTo(final Value object) {
        if (object instanceof Numeric) {
            return Double.compare(value, ((Numeric) object).value);
        }
        return super.compareTo(object);
    }

    @Override
    public boolean equals(final Object object) {
        if (object instanceof Numeric) {
            return this.compareTo((Numeric) object) == 0;
        }
        return super.equals(object);
    }

    @Override
    public int hashCode() {
        return Double.valueOf(this.value).hashCode();
    }

    @Override
    public Value duplicate() {
        Numeric duplicate = this.pool.create();
        duplicate.set(value);
        return duplicate;
    }

    @Override
    public double toDouble() {
        return value;
    }

    public void attachToPool(final PoolProvider poolProvider) {
        this.pool = poolProvider.getPool(Numeric.class);
    }

    @Override
    protected void relinquish() {
        this.pool.destroy(this);
    }

    @Override
    public Type getType() {
        return Type.NUMBER;
    }

    @Override
    public boolean toBoolean() {
        return value != 0;
    }
}
