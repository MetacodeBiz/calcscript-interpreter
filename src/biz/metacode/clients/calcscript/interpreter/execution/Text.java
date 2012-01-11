
package biz.metacode.clients.calcscript.interpreter.execution;

public class Text implements Value {

    private static final long serialVersionUID = 1168274352878421240L;

    private String value;

    private final transient TextPool pool;

    Text(TextPool pool, String value) {
        this.pool = pool;
        this.value = value;
    }

    public String consume() {
        String value = this.value;
        this.pool.relinquish(this);
        return value;
    }

    void set(String string) {
        this.value = string;
    }

    @Override
    public String toString() {
        return value;
    }
}
