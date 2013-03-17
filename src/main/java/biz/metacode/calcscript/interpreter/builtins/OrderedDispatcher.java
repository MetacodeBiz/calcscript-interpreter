
package biz.metacode.calcscript.interpreter.builtins;

import biz.metacode.calcscript.interpreter.ExecutionContext;
import biz.metacode.calcscript.interpreter.Value;
import biz.metacode.calcscript.interpreter.Value.Pair;

/**
 * Function dispatcher that orders two top stack values before executing a call
 * to target method.
 */
public final class OrderedDispatcher extends FunctionDispatcher {

    private static final long serialVersionUID = 5670005527311115309L;

    /**
     * Create an ordered dispatcher for given operator prefix.
     *
     * @param prefix Operator prefix.
     */
    public OrderedDispatcher(final String prefix) {
        super(prefix);
    }

    /**
     * Orders the arguments according to theirs priority.
     *
     * @param context Execution context.
     * @param first First value.
     * @param second Second value.
     * @return Ordered pair of values.
     * @see Value#getPriority()
     */
    @Override
    protected Pair transform(final ExecutionContext context, final Value first,
            final Value second) {
        return first.order(second);
    }

}
