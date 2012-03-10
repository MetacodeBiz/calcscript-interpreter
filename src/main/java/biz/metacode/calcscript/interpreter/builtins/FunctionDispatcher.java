
package biz.metacode.calcscript.interpreter.builtins;

import biz.metacode.calcscript.interpreter.ExecutionContext;
import biz.metacode.calcscript.interpreter.Invocable;
import biz.metacode.calcscript.interpreter.OverloadMissingException;
import biz.metacode.calcscript.interpreter.Value;
import biz.metacode.calcscript.interpreter.Value.Pair;

/**
 * Allows overriding function names by dispatching call to function based on the
 * top two values on the stack.
 */
public abstract class FunctionDispatcher implements Invocable {

    private static final long serialVersionUID = -1053849869084680144L;

    private final String prefix;

    /**
     * Create a dispatcher for given operator prefix.
     *
     * @param prefix Operator prefix.
     */
    protected FunctionDispatcher(String prefix) {
        this.prefix = prefix;
    }

    public final void invoke(ExecutionContext context) throws InterruptedException {

        Value first = context.pop();
        Value second = context.pop();
        Pair ordered = this.transform(context, first, second);

        String methodName = prefix + "_" + ordered.getTypeName();

        context.push(ordered.second);
        context.push(ordered.first);

        second.release();
        first.release();

        Invocable targetMethod = context.read(methodName);
        if (targetMethod != null) {
            targetMethod.invoke(context);
        } else {
            throw new OverloadMissingException(ordered.second.getType(), ordered.first.getType());
        }
    }

    /**
     * Change or reorder arguments of the call.
     *
     * @param context Execution context.
     * @param first First argument of the call.
     * @param second Second argument of the call.
     * @return Actual pair of values that will be used to make the call.
     */
    protected abstract Pair transform(ExecutionContext context, Value first, Value second);

}
