
package biz.metacode.clients.calcscript.interpreter.execution;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Memory implements Serializable {

    private static final long serialVersionUID = 2268129176519901123L;

    private final Map<String, Serializable> data = new HashMap<String, Serializable>();

    public void write(String name, Serializable data) {
        this.data.put(name, data);
    }

    public Serializable read(String name) {
        return this.data.get(name);
    }

    public Set<String> keys() {
        return this.data.keySet();
    }

}
