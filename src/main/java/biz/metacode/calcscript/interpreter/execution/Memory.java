
package biz.metacode.calcscript.interpreter.execution;

import biz.metacode.calcscript.interpreter.Invocable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

class Memory implements Serializable, Iterable<Map.Entry<String, Invocable>> {

    private static final long serialVersionUID = 2268129176519901123L;

    private final Map<String, Invocable> data = new HashMap<String, Invocable>();

    public Invocable write(final String name, final Invocable invocable) {
        return this.data.put(name, invocable);
    }

    public Invocable read(final String name) {
        return this.data.get(name);
    }

    public Set<String> keys() {
        return this.data.keySet();
    }

    public Iterator<Map.Entry<String, Invocable>> iterator() {
        return this.data.entrySet().iterator();
    }

    public Invocable remove(final String name) {
        return this.data.remove(name);
    }

}
