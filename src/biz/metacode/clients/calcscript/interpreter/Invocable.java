
package biz.metacode.clients.calcscript.interpreter;

import biz.metacode.clients.calcscript.interpreter.execution.Context;

import java.io.Serializable;

public interface Invocable extends Serializable {
    void invoke(Context context);
}
