
package biz.metacode.clients.calcscript.interpreter.execution;

import java.util.LinkedList;

public class TextPool {

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

    void relinquish(Text text) {
        this.pool.push(text);
    }
}
