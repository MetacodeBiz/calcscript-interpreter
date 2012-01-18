
package biz.metacode.clients.calcscript.interpreter.execution;

import java.util.LinkedList;

public class TextPool implements Pool<Text> {

    private LinkedList<Text> pool = new LinkedList<Text>();

    TextPool() {

    }

    public Text create(String string) {
        Text text = pool.poll();
        if (text != null) {
            text.set(string);
            return text;
        }
        return new Text(this, string);
    }

    public void destroy(Text text) {
        assert !containsIdentical(text) : "Releasing twice the same object!";
        this.pool.add(text);
    }

    public Text create() {
        return create(null);
    }

    @Override
    public String toString() {
        return "TextPool: " + pool;
    }

    private boolean containsIdentical(Object o) {
        for (Object obj : pool) {
            if (obj == o) {
                return true;
            }
        }
        return false;
    }

    int internalGetPooledObjectsCount() {
        return pool.size();
    }
}
