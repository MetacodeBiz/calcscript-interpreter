
package biz.metacode.clients.calcscript.interpreter.execution;

import biz.metacode.clients.calcscript.interpreter.Value;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Numeric extends RefCountedValue implements PooledObject {

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

    public double consume() {
        double result = value;
        this.release();
        return result;
    }

    void set(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return threeDec.format(value);
    }

    @Override
    protected int getPriority() {
        return 1;
    }

    @Override
    public Value duplicate() {
        Numeric duplicate = this.pool.acquire();
        duplicate.set(value);
        return duplicate;
    }

    public void attachToPool(PoolProvider poolProvider) {
        this.pool = poolProvider.getPool(Numeric.class);
    }

    @Override
    protected void relinquish() {
        this.pool.relinquish(this);
    }
}
