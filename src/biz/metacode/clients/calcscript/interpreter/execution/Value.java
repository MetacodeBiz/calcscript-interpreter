
package biz.metacode.clients.calcscript.interpreter.execution;

import java.text.DecimalFormat;

public class Value {

    private final static DecimalFormat threeDec = new DecimalFormat("0.###");

    private final ValuePool pool;

    private double value;

    public Value(ValuePool pool, double value) {
        this.pool = pool;
        this.value = value;
    }

    public double consume() {
        double result = value;
        this.pool.relinquish(this);
        return result;
    }

    void set(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return threeDec.format(value);
    }
}
