
package biz.metacode.clients.calcscript.interpreter;

import java.io.Serializable;

public interface Invocable extends Serializable {
    void invoke(ExecutionContext context);
}
