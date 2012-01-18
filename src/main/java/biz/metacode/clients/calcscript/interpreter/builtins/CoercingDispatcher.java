
package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.ExecutionContext;
import biz.metacode.clients.calcscript.interpreter.Value;
import biz.metacode.clients.calcscript.interpreter.Value.Pair;

public class CoercingDispatcher extends FunctionDispatcher {

    private static final long serialVersionUID = -3727454201677770896L;

    public CoercingDispatcher(String prefix) {
        super(prefix);
    }

    @Override
    protected Pair transform(ExecutionContext context, Value first, Value second) {
        return context.coerce(first, second);
    }

}
