package biz.metacode.clients.calcscript.interpreter.execution;

import java.util.LinkedList;

public class NumericPool implements Pool<Numeric> {

    private final LinkedList<Numeric> ownedValues = new LinkedList<Numeric>();

    public Numeric acquire(double value) {
        Numeric cachedValue = ownedValues.poll();
        if (cachedValue != null) {
            cachedValue.acquire();
            cachedValue.set(value);
            return cachedValue;
        }
        return new Numeric(this, value);
    }

    public void relinquish(Numeric value) {
        assert !ownedValues.contains(value): "Releasing twice the same object!";
        ownedValues.add(value);
    }

    public Numeric acquire() {
        return acquire(0);
    }

}
