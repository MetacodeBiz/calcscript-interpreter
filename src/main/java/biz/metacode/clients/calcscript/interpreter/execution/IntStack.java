
package biz.metacode.clients.calcscript.interpreter.execution;

class IntStack {

    private final int[] data = new int[10];

    private int pointer = -1;

    public void push(int element) {
        data[++pointer] = element;
    }

    public int pop() {
        return data[pointer--];
    }

    public int peek() {
        return data[pointer];
    }

    public void setLast(int element) {
        if (!isEmpty()) {
            data[pointer] = element;
        }
    }

    public boolean isEmpty() {
        return pointer < 0;
    }
}
