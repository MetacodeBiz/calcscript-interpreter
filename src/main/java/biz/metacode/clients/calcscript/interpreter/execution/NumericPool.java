
package biz.metacode.clients.calcscript.interpreter.execution;

import java.util.LinkedList;

public class NumericPool implements Pool<Numeric> {

    private final LinkedList<Numeric> ownedValues = new LinkedList<Numeric>();

    public Numeric create(double value) {
        Numeric cachedValue = ownedValues.poll();
        if (cachedValue != null) {
            cachedValue.set(value);
            return cachedValue;
        }
        return new Numeric(this, value);
    }

    public void destroy(Numeric value) {
        assert !ownedValues.contains(value) : "Releasing twice the same object!";
        ownedValues.add(value);
    }

    public Numeric create() {
        return create(0);
    }

    @Override
    public String toString() {
        return "NumericPool: " + ownedValues;
    }

    int internalGetPooledObjectsCount() {
        return ownedValues.size();
    }
}
