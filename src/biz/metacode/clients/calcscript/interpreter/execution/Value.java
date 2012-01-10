
package biz.metacode.clients.calcscript.interpreter.execution;

import java.io.Serializable;
import java.text.DecimalFormat;

public class Value implements Serializable {

    private static final long serialVersionUID = -7340834444775795549L;

    private final static DecimalFormat threeDec = new DecimalFormat("0.###");

    private transient final ValuePool pool;

    private double value;

    public Value(ValuePool pool, double value) {
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
}