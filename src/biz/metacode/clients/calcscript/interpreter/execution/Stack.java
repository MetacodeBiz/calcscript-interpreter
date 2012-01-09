
package biz.metacode.clients.calcscript.interpreter.execution;

import java.io.Serializable;
import java.util.List;

public class Stack {

    private final java.util.Stack<Serializable> data = new java.util.Stack<Serializable>();

    private int mark = 0;

    private final ArrayPool pool;

    public Stack(ArrayPool pool) {
        this.pool = pool;
    }

    public void push(Serializable element) {
        this.data.push(element);
    }

    public Serializable pop() {
        if (mark > this.data.size() - 1)
            mark--;
        return this.data.pop();
    }

    public <T extends Serializable> T pop(Class<T> type) {
        Serializable value = this.pop();
        return type.cast(value);
    }

    public Serializable peek() {
        return this.data.peek();
    }

    public Serializable popAt(int index) {
        if (mark > this.data.size() - 1)
            mark--;
        return this.data.remove(index);
    }

    public Serializable peekAt(int index) {
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

    public List<Serializable> getData() {
        return data;
    }
}
