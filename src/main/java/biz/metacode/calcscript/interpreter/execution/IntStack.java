
package biz.metacode.calcscript.interpreter.execution;

class IntStack {

    public static final int STACK_SIZE = 10;

    private final int[] data = new int[STACK_SIZE];

    private int pointer = -1;

    public void push(final int element) {
        data[++pointer] = element;
    }

    public int pop() {
        return data[pointer--];
    }

    public int peek() {
        return data[pointer];
    }

    public void setLast(final int element) {
        if (!isEmpty()) {
            data[pointer] = element;
        }
    }

    public boolean isEmpty() {
        return pointer < 0;
    }
}
