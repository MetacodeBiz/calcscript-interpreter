
package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.ExecutionContext;
import biz.metacode.clients.calcscript.interpreter.Value;
import biz.metacode.clients.calcscript.interpreter.Value.Pair;

public class OrderedDispatcher extends FunctionDispatcher {

    private static final long serialVersionUID = 5670005527311115309L;

    public OrderedDispatcher(String prefix) {
        super(prefix);
    }

    @Override
    protected Pair transform(ExecutionContext context, Value first, Value second) {
        return first.order(second);
    }

}