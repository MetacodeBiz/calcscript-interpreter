
package biz.metacode.calcscript.interpreter.execution;

import biz.metacode.calcscript.interpreter.Value;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

class Numeric extends RefCountedValue implements PooledObject {

    private static final long serialVersionUID = -7340834444775795549L;

    private final static DecimalFormat threeDec;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        threeDec = new DecimalFormat("0.###", symbols);
    }

    private transient Pool<Numeric> pool;

    private double value;

    Numeric(Pool<Numeric> pool, double value) {
        this.pool = pool;
        this.value = value;
    }

    public double get() {
        return value;
    }

    void set(double value) {
        if (isShared()) {
            throw new IllegalStateException("Object is shared.");
        }
        this.value = value;
    }

    @Override
    public String toString() {
        return threeDec.format(value);
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public int compareTo(Value o) {
        if (o instanceof Numeric) {
            return Double.compare(value, ((Numeric) o).value);
        }
        return super.compareTo(o);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Numeric) {
            return this.value == ((Numeric) obj).value;
        }
        return super.equals(obj);
    }

    @Override
    public Value duplicate() {
        Numeric duplicate = this.pool.create();
        duplicate.set(value);
        return duplicate;
    }

    public double toDouble() {
        return value;
    }

    public void attachToPool(PoolProvider poolProvider) {
        this.pool = poolProvider.getPool(Numeric.class);
    }

    @Override
    protected void relinquish() {
        this.pool.destroy(this);
    }

    @Override
    public String getTypeName() {
        return "number";
    }

    @Override
    public boolean toBoolean() {
        return value != 0;
    }
}