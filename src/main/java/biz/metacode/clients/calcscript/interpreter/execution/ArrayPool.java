
package biz.metacode.clients.calcscript.interpreter.execution;

import java.util.LinkedList;

public class ArrayPool implements Pool<Array> {

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

    public void destroy(Array value) {
        assert !containsIdentical(value) : "Releasing twice the same object!";
        allocationBalance--;
        value.clear();
        ownedValues.add(value);
    }

    @Override
    public String toString() {
        return "ArrayPool: " + ownedValues;
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

    void internalResetAllocationBalance() {
        allocationBalance = 0;
    }

    int internalGetAllocationBalance() {
        return allocationBalance;
    }

}
