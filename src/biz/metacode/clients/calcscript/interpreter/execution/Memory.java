
package biz.metacode.clients.calcscript.interpreter.execution;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Memory implements Serializable {

    private static final long serialVersionUID = 2268129176519901123L;

    private final Map<String, Value> data = new HashMap<String, Value>();

    public void write(String name, Value data) {
        this.data.put(name, data);
    }

    public Value read(String name) {
        return this.data.get(name);
    }

    public Set<String> keys() {
        return this.data.keySet();
    }

}
