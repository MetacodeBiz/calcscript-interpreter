
package biz.metacode.clients.calcscript.interpreter.execution;

import java.io.Serializable;
import java.util.List;

public class Context {
    private final Memory memory;

    private final NumericPool valuePool = new NumericPool();

    private final ArrayPool arrayPool = new ArrayPool();

    private final TextPool textPool = new TextPool();

    private Stack stack;

    public Context(Memory memory) {
        this.memory = memory;
        this.clearStack();
    }

    public void clearStack() {
        stack = new Stack(arrayPool);
    }

    public List<Value> getData() {
        return stack.getData();
    }

    public void write(String name, Value data) {
        this.memory.write(name, data);
    }

    public Serializable read(String name) {
        return this.memory.read(name);
    }

    public void push(Value element) {
        this.stack.push(element);
    }

    public void push(double element) {
        this.stack.push(valuePool.acquire(element));
    }

    public void push(String element) {
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
}
