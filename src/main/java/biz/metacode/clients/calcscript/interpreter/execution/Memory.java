
package biz.metacode.clients.calcscript.interpreter.execution;

import biz.metacode.clients.calcscript.interpreter.Invocable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Memory implements Serializable, Iterable<Map.Entry<String, Invocable>> {

    private static final long serialVersionUID = 2268129176519901123L;

    private final Map<String, Invocable> data = new HashMap<String, Invocable>();

    public Invocable write(String name, Invocable data) {
        return this.data.put(name, data);
    }

    public Invocable read(String name) {
        return this.data.get(name);
    }

    public Set<String> keys() {
        return this.data.keySet();
    }

    public Iterator<Map.Entry<String, Invocable>> iterator() {
        return this.data.entrySet().iterator();
    }

    public Invocable remove(String name) {
        return this.data.remove(name);
    }

}
