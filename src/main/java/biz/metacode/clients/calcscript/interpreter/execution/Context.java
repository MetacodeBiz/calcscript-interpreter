
package biz.metacode.clients.calcscript.interpreter.execution;

import biz.metacode.clients.calcscript.interpreter.ExecutionContext;
import biz.metacode.clients.calcscript.interpreter.Invocable;
import biz.metacode.clients.calcscript.interpreter.Value;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Context implements ExecutionContext, PoolProvider {

    private final NumericPool valuePool = new NumericPool();

    private final ArrayPool arrayPool = new ArrayPool();

    private final TextPool textPool = new TextPool();

    private Stack stack;

    private Memory memory;

    public Context() {
        this.clearStack();
    }

    public void setMemory(Memory memory) {
        this.memory = memory;
    }

    public void clearStack() {
        stack = new Stack(arrayPool);
    }

    public Array getData() {
        return stack.getData();
    }

    public void write(String name, Invocable element) {
        if (element instanceof RefCountedValue) {
            ((RefCountedValue) element).acquire();
        }
        Invocable previous = this.memory.write(name, element);
        if (previous instanceof Value) {
            ((Value) previous).release();
        }
    }

    public Invocable read(String name) {
        return this.memory.read(name);
    }

    public void push(Value element) {
        if (element instanceof RefCountedValue) {
            ((RefCountedValue) element).acquire();
        }
        this.stack.push(element);
    }

    public void pushDouble(double element) {
        this.push(valuePool.create(element));
    }

    public void pushString(String element) {
        this.push(textPool.create(element));
    }

    public Value pop() {
        return stack.pop();
    }

    public double popDouble() {
        Numeric result = (Numeric) pop();
        try {
            return result.get();
        } finally {
            result.release();
        }
    }

    public String popString() {
        Text result = (Text) pop();
        try {
            return result.get();
        } finally {
            result.release();
        }
    }

    public Value peek() {
        return stack.peek();
    }

    public Value popAt(int index) {
        return stack.popAt(index);
    }

    public Value peekAt(int index) {
        return stack.peekAt(index);
    }

    public void markPosition() {
        stack.markPosition();
    }

    public Array extractMarkedArray() {
        return stack.extractMarkedArray();
    }

    public Array acquireArray() {
        return arrayPool.create();
    }

    public void pushArray(Collection<? extends Value> array) {
        if (array instanceof Array) {
            this.push((Array) array);
        } else {
            Array temporary = this.acquireArray();
            temporary.addAll(array);
            this.push(temporary);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends PooledObject> Pool<T> getPool(Class<T> pooledObject) {
        if (Numeric.class.equals(pooledObject)) {
            return (Pool<T>) valuePool;
        }
        if (Text.class.equals(pooledObject)) {
            return (Pool<T>) textPool;
        }
        if (Array.class.equals(pooledObject)) {
            return (Pool<T>) arrayPool;
        }
        throw new IllegalArgumentException("This type of pool is not supported: " + pooledObject);
    }

    public Set<String> getRegisteredVariableNames() {
        return memory.keys();
    }

    public Iterator<Map.Entry<String, Invocable>> getRegisteredVariables() {
        return memory.iterator();
    }
}