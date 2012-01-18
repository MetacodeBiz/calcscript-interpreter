
package biz.metacode.clients.calcscript.interpreter.execution;

import biz.metacode.clients.calcscript.interpreter.Value;

public class Stack {

    private final java.util.Stack<Value> data = new java.util.Stack<Value>();

    private final Pool<Array> pool;

    private final IntStack marks = new IntStack();

    public Stack(Pool<Array> pool) {
        this.pool = pool;
    }

    public void push(Value element) {
        this.data.push(element);
    }

    public Value pop() {
        adjustMark();
        return this.data.pop();
    }

    private void adjustMark() {
        if (!marks.isEmpty() && marks.peek() > this.data.size() - 1) {
            marks.push(marks.pop() - 1);
        }
    }

    public <T extends Value> T pop(Class<T> type) {
        Value value = this.pop();
        return type.cast(value);
    }

    public Value peek() {
        return this.data.peek();
    }

    public Value popAt(int index) {
        adjustMark();
        return this.data.remove(this.data.size() - index - 1);
    }

    public Value peekAt(int index) {
        return this.data.get(this.data.size() - index - 1);
    }

    public void markPosition() {
        this.marks.push(this.data.size());
    }

    public Array extractMarkedArray() {
        int mark = marks.isEmpty() ? 0 : marks.pop();
        Array part = pool.create();
        for (int i = mark, l = this.data.size(); i < l; i++) {
            part.add(this.data.remove(mark));
        }
        return part;
    }

    public Array getData() {
        Array data = pool.create();
        data.acquire();
        data.addAll(this.data);
        this.data.clear();
        return data;
    }
}
