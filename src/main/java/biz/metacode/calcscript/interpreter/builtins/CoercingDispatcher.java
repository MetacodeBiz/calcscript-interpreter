
package biz.metacode.calcscript.interpreter.builtins;

import biz.metacode.calcscript.interpreter.ExecutionContext;
import biz.metacode.calcscript.interpreter.Value;
import biz.metacode.calcscript.interpreter.Value.Pair;

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
