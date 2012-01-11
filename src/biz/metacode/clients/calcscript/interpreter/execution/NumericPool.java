package biz.metacode.clients.calcscript.interpreter.execution;

import java.util.LinkedList;

public class NumericPool implements Pool<Numeric> {

    private final LinkedList<Numeric> ownedValues = new LinkedList<Numeric>();

    public Numeric acquire(double value) {
        Numeric cachedValue = ownedValues.poll();
        if (cachedValue != null) {
            cachedValue.set(value);
            return cachedValue;
        }
        return new Numeric(this, value);
    }

    public void relinquish(Numeric value) {
        ownedValues.push(value);
    }

    @Override
    public Numeric acquire() {
        return acquire(0);
    }

}
