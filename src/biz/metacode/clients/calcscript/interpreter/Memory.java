
package biz.metacode.clients.calcscript.interpreter;

import java.util.HashMap;
import java.util.Map;

public class Memory {

    private final Map<String, Object> data = new HashMap<String, Object>();

    public void write(String name, Object data) {
        this.data.put(name, data);
    }

    public Object read(String name) {
        return this.data.get(name);
    }

}
