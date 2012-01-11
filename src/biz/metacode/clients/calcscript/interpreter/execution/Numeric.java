
package biz.metacode.clients.calcscript.interpreter.execution;

import biz.metacode.clients.calcscript.interpreter.Value;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Numeric extends Value {

    private static final long serialVersionUID = -7340834444775795549L;

    private final static DecimalFormat threeDec;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        threeDec = new DecimalFormat("0.###", symbols);
    }

    private transient final Pool<Numeric> pool;

    private double value;

    Numeric(Pool<Numeric> pool, double value) {
        this.pool = pool;
        this.value = value;
    }

    public double consume() {
        double result = value;
        if (this.pool != null) {
            this.pool.relinquish(this);
        }
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
}
