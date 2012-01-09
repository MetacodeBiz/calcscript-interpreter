
package biz.metacode.clients.calcscript.interpreter.execution;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Memory {

    private final Map<String, Object> data = new HashMap<String, Object>();

    public void write(String name, Object data) {
        this.data.put(name, data);
    }

    public Object read(String name) {
        return this.data.get(name);
    }

    public Set<String> keys() {
        return this.data.keySet();
    }

}
