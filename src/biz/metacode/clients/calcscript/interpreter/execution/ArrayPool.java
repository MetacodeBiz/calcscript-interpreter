package biz.metacode.clients.calcscript.interpreter.execution;

import java.util.LinkedList;

public class ArrayPool implements Pool<Array> {

    private final LinkedList<Array> ownedValues = new LinkedList<Array>();

    public Array acquire() {
        Array cachedValue = ownedValues.poll();
        if (cachedValue != null) {
            cachedValue.clear();
            return cachedValue;
        }
        return new Array(this);
    }

    public void relinquish(Array value) {
        ownedValues.push(value);
    }

}
