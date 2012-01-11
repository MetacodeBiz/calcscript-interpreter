
package biz.metacode.clients.calcscript.interpreter;

import biz.metacode.clients.calcscript.interpreter.execution.Context;
import biz.metacode.clients.calcscript.interpreter.execution.Value;

public interface Executable extends Value {
    void execute(Context context);
}
