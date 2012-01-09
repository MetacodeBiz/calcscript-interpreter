
package biz.metacode.clients.calcscript.interpreter.execution;

import java.util.List;

public class Context {
    private final Memory memory;

    private final ValuePool valuePool = new ValuePool();

    private Stack stack;

    public Context(Memory memory) {
        this.memory = memory;
        this.clearStack();
    }

    public void clearStack() {
        stack = new Stack();
    }

    public List<Object> getData() {
        return stack.getData();
    }

    public void write(String name, Object data) {
        this.memory.write(name, data);
    }

    public Object read(String name) {
        return this.memory.read(name);
    }

    public void push(Object element) {
        this.stack.push(element);
    }

    public void pushDouble(double element) {
        this.stack.push(valuePool.acquire(element));
    }

    public Object pop() {
        return stack.pop();
    }

    public double popDouble() {
        return stack.pop(Value.class).consume();
    }

    public Object peek() {
        return stack.peek();
    }

    public Object popAt(int index) {
        return stack.popAt(index);
    }

    public Object peekAt(int index) {
        return stack.peekAt(index);
    }

    public void markPosition() {
        stack.markPosition();
    }

    public List<Object> extractMarkedArray() {
        return stack.extractMarkedArray();
    }
}
