
package biz.metacode.calcscript.interpreter.execution;

import biz.metacode.calcscript.interpreter.Value;

class Stack {

    private final java.util.Stack<Value> data = new java.util.Stack<Value>();

    private final Pool<Array> pool;

    private final IntStack marks = new IntStack();

    public Stack(final Pool<Array> pool) {
        this.pool = pool;
    }

    public void push(final Value element) {
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

    public <T extends Value> T pop(final Class<T> type) {
        Value value = this.pop();
        return type.cast(value);
    }

    public Value peek() {
        return this.data.peek();
    }

    public Value popAt(final int index) {
        adjustMark();
        return this.data.remove(this.data.size() - index - 1);
    }

    public Value peekAt(final int index) {
        return this.data.get(this.data.size() - index - 1);
    }

    public void markPosition() {
        this.marks.push(this.data.size());
    }

    public Array extractMarkedArray() {
        int mark = 0;
        if (!marks.isEmpty()) {
            mark = marks.pop();
        }
        Array part = pool.create();
        for (int i = mark, l = this.data.size(); i < l; i++) {
            Value value = this.data.remove(mark);
            part.add(value);
            value.release();
        }
        return part;
    }

    public Array getData() {
        Array result = pool.create();
        result.addAll(this.data);
        for (Value value : this.data) {
            value.release();
        }
        this.data.clear();
        result.acquire();
        return result;
    }
}
