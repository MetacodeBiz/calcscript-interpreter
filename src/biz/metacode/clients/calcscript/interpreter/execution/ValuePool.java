package biz.metacode.clients.calcscript.interpreter.execution;

import java.util.LinkedList;

public class ValuePool {

    private final LinkedList<Value> ownedValues = new LinkedList<Value>();

    public Value acquire(double value) {
        Value cachedValue = ownedValues.poll();
        if (cachedValue != null) {
            cachedValue.set(value);
            return cachedValue;
        }
        return new Value(this, value);
    }

    void relinquish(Value value) {
        ownedValues.push(value);
    }

}
