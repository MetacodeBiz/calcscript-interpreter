
package biz.metacode.clients.calcscript.interpreter.execution;

import biz.metacode.clients.calcscript.interpreter.ExecutionContext;
import biz.metacode.clients.calcscript.interpreter.Invocable;
import biz.metacode.clients.calcscript.interpreter.Value;

import java.util.Collection;

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

    public void write(String name, Invocable data) {
        this.memory.write(name, data);
    }

    public Invocable read(String name) {
        return this.memory.read(name);
    }

    public void push(Value element) {
        this.stack.push(element);
    }

    public void pushDouble(double element) {
        this.stack.push(valuePool.acquire(element));
    }

    public void pushString(String element) {
        this.stack.push(textPool.acquire(element));
    }

    public Value pop() {
        return stack.pop();
    }

    public double popDouble() {
        return stack.pop(Numeric.class).consume();
    }

    public String popString() {
        return stack.pop(Text.class).consume();
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
        return arrayPool.acquire();
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
}
