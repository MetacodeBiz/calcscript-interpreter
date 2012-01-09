
package biz.metacode.clients.calcscript.interpreter.execution;

import java.util.ArrayList;
import java.util.List;

public class Stack {

    private final java.util.Stack<Object> data = new java.util.Stack<Object>();

    private int mark = 0;

    public void push(Object element) {
        this.data.push(element);
    }

    public Object pop() {
        if (mark > this.data.size() - 1)
            mark--;
        return this.data.pop();
    }

    public <T> T pop(Class<T> type) {
        Object value = this.pop();
        return type.cast(value);
    }

    public Object peek() {
        return this.data.peek();
    }

    public Object popAt(int index) {
        if (mark > this.data.size() - 1)
            mark--;
        return this.data.remove(index);
    }

    public Object peekAt(int index) {
        return this.data.get(index);
    }

    public void markPosition() {
        this.mark = this.data.size();
    }

    public List<Object> extractMarkedArray() {
        List<Object> part = new ArrayList<Object>();
        for (int i = this.mark, l = this.data.size(); i < l; i++) {
            part.add(this.data.remove(this.mark));
        }
        return part;
    }

    public List<Object> getData() {
        return data;
    }
}
