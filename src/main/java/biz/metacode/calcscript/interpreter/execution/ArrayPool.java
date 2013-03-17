
package biz.metacode.calcscript.interpreter.execution;

import java.util.LinkedList;

class ArrayPool implements Pool<Array> {

    private String trait;

    private int allocationBalance;

    private final LinkedList<Array> ownedValues = new LinkedList<Array>();

    public Array create() {
        allocationBalance++;
        Array cachedValue = ownedValues.poll();
        if (cachedValue != null) {
            return cachedValue;
        }
        Array array = new Array(this);
        array.trait = trait;
        return array;
    }

    public void destroy(final Array value) {
        assert !containsIdentical(value) : "Releasing twice the same object!";
        allocationBalance--;
        value.clear();
        ownedValues.add(value);
    }

    @Override
    public String toString() {
        return "ArrayPool: " + ownedValues;
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
