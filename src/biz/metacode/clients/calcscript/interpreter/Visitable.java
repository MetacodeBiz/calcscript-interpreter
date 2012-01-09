
package biz.metacode.clients.calcscript.interpreter;

import biz.metacode.clients.calcscript.interpreter.execution.Context;

import java.io.Serializable;

public interface Visitable extends Serializable {
    void visit(Context context);
}
