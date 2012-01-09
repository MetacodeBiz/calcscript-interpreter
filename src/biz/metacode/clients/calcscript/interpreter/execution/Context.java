
package biz.metacode.clients.calcscript.interpreter.execution;

import java.io.Serializable;
import java.util.List;

public class Context {
    private final Memory memory;

    private final ValuePool valuePool = new ValuePool();

    private final ArrayPool arrayPool = new ArrayPool();

    private Stack stack;

    public Context(Memory memory) {
        this.memory = memory;
        this.clearStack();
    }

    public void clearStack() {
        stack = new Stack(arrayPool);
    }

    public List<Serializable> getData() {
        return stack.getData();
    }

    public void write(String name, Serializable data) {
        this.memory.write(name, data);
    }

    public Serializable read(String name) {
        return this.memory.read(name);
    }

    public void push(Serializable element) {
        this.stack.push(element);
    }

    public void pushDouble(double element) {
        this.stack.push(valuePool.acquire(element));
    }

    public Serializable pop() {
        return stack.pop();
    }

    public double popDouble() {
        return stack.pop(Value.class).consume();
    }

    public Serializable peek() {
        return stack.peek();
    }

    public Serializable popAt(int index) {
        return stack.popAt(index);
    }

    public Serializable peekAt(int index) {
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
}
