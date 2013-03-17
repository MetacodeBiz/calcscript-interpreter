
package biz.metacode.calcscript.interpreter.builtins;

import biz.metacode.calcscript.interpreter.ExecutionContext;
import biz.metacode.calcscript.interpreter.Value;
import biz.metacode.calcscript.interpreter.Value.Pair;

/**
 * Function dispatcher that coerces two values on top of the stack before
 * calling target operator.
 */
public final class CoercingDispatcher extends FunctionDispatcher {

    private static final long serialVersionUID = -3727454201677770896L;

    /**
     * Create a coercing dispatcher for given operator prefix.
     *
     * @param prefix Operator prefix.
     */
    public CoercingDispatcher(final String prefix) {
        super(prefix);
    }

    /**
     * Coerces two arguments to be of the same type.
     *
     * @param context Execution context
     * @param first First value
     * @param second Second value
     * @return Pair of coerced values
     * @see ExecutionContext#coerce(Value, Value)
     */
    @Override
    protected Pair transform(final ExecutionContext context, final Value first,
            final Value second) {
        return context.coerce(first, second);
    }

}
