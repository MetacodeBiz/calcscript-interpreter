
package biz.metacode.calcscript.interpreter.execution;

import java.util.LinkedList;

class NumericPool implements Pool<Numeric> {

    private String trait;

    private int allocationBalance;

    private final LinkedList<Numeric> ownedValues = new LinkedList<Numeric>();

    public Numeric create(final double value) {
        allocationBalance++;
        Numeric cachedValue = ownedValues.poll();
        if (cachedValue != null) {
            cachedValue.set(value);
            return cachedValue;
        }
        Numeric numeric = new Numeric(this, value);
        numeric.trait = trait;
        return numeric;
    }

    public void destroy(final Numeric value) {
        assert !containsIdentical(value) : "Releasing twice the same object!";
        assert !value.isShared() : "Releasing shared object!";
        allocationBalance--;
        ownedValues.add(value);
    }

    public Numeric create() {
        return create(0);
    }

    @Override
    public String toString() {
        return "NumericPool: " + ownedValues;
    }

    private boolean containsIdentical(final Object object) {
        for (Object obj : ownedValues) {
            if (obj == object) {
                return true;
            }
        }
        return false;
    }

    int internalGetPooledObjectsCount() {
        return ownedValues.size();
    }

    public void setTrait(final String trait) {
        this.trait = trait;
        for (RefCountedValue value : ownedValues) {
            value.trait = trait;
        }
    }

    void internalResetAllocationBalance() {
        allocationBalance = 0;
    }

    int internalGetAllocationBalance() {
        return allocationBalance;
    }
}
