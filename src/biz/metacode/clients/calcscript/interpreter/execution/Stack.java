
package biz.metacode.clients.calcscript.interpreter.execution;

import java.util.List;

public class Stack {

    private final java.util.Stack<Value> data = new java.util.Stack<Value>();

    private int mark = 0;

    private final ArrayPool pool;

    public Stack(ArrayPool pool) {
        this.pool = pool;
    }

    public void push(Value element) {
        this.data.push(element);
    }

    public Value pop() {
        if (mark > this.data.size() - 1)
            mark--;
        return this.data.pop();
    }

    public <T extends Value> T pop(Class<T> type) {
        Value value = this.pop();
        return type.cast(value);
    }

    public Value peek() {
        return this.data.peek();
    }

    public Value popAt(int index) {
        if (mark > this.data.size() - 1)
            mark--;
        return this.data.remove(index);
    }

    public Value peekAt(int index) {
        return this.data.get(index);
    }

    public void markPosition() {
        this.mark = this.data.size();
    }

    public Array extractMarkedArray() {
        Array part = pool.acquire();
        for (int i = this.mark, l = this.data.size(); i < l; i++) {
            part.add(this.data.remove(this.mark));
        }
        return part;
    }

    public List<Value> getData() {
        return data;
    }
}
