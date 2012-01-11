
package biz.metacode.clients.calcscript.interpreter;

import java.util.Collection;

public interface SharedArray extends Collection<Value> {
    void release();
}
