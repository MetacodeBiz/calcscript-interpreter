
package biz.metacode.clients.calcscript.interpreter.execution;

import java.util.LinkedList;

public class NumericPool implements Pool<Numeric> {

    private String trait;

    private final LinkedList<Numeric> ownedValues = new LinkedList<Numeric>();

    public Numeric create(double value) {
        Numeric cachedValue = ownedValues.poll();
        if (cachedValue != null) {
            cachedValue.set(value);
            return cachedValue;
        }
        Numeric numeric = new Numeric(this, value);
        numeric.trait = trait;
        return numeric;
    }

    public void destroy(Numeric value) {
        assert !containsIdentical(value) : "Releasing twice the same object!";
        assert !value.isShared() : "Releasing shared object!";
        ownedValues.add(value);
    }

    public Numeric create() {
        return create(0);
    }

    @Override
    public String toString() {
        return "NumericPool: " + ownedValues;
    }

    private boolean containsIdentical(Object o) {
        for (Object obj : ownedValues) {
            if (obj == o) {
                return true;
            }
        }
        return false;
    }

    int internalGetPooledObjectsCount() {
        return ownedValues.size();
    }

    public void setTrait(String trait) {
        this.trait = trait;
        for (RefCountedValue value : ownedValues) {
            value.trait = trait;
        }
    }
}
