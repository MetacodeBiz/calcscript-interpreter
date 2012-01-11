package biz.metacode.clients.calcscript.interpreter.execution;

import java.util.LinkedList;

public class NumericPool {

    private final LinkedList<Numeric> ownedValues = new LinkedList<Numeric>();

    public Numeric acquire(double value) {
        Numeric cachedValue = ownedValues.poll();
        if (cachedValue != null) {
            cachedValue.set(value);
            return cachedValue;
        }
        return new Numeric(this, value);
    }

    void relinquish(Numeric value) {
        ownedValues.push(value);
    }

}
