
package biz.metacode.clients.calcscript.interpreter.execution;

import java.util.LinkedList;

public class TextPool implements Pool<Text> {

    private LinkedList<Text> pool = new LinkedList<Text>();

    TextPool() {

    }

    public Text acquire(String string) {
        Text text = pool.poll();
        if (text != null) {
            text.set(string);
            return text;
        }
        return new Text(this, string);
    }

    public void relinquish(Text text) {
        assert !pool.contains(text): "Releasing twice the same object!";
        this.pool.add(text);
    }

    public Text acquire() {
        return acquire(null);
    }
}
