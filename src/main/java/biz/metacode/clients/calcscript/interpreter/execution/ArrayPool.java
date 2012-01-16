package biz.metacode.clients.calcscript.interpreter.execution;

import java.util.LinkedList;

public class ArrayPool implements Pool<Array> {

    private final LinkedList<Array> ownedValues = new LinkedList<Array>();

    public Array create() {
        Array cachedValue = ownedValues.poll();
        if (cachedValue != null) {
            return cachedValue;
        }
        return new Array(this);
    }

    public void destroy(Array value) {
        assert !ownedValues.contains(value): "Releasing twice the same object!";
        value.clear();
        ownedValues.add(value);
    }

    @Override
    public String toString() {
        return "ArrayPool: " + ownedValues;
    }

    int internalGetPooledObjectsCount() {
        return ownedValues.size();
    }

}
