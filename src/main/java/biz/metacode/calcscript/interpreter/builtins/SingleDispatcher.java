
package biz.metacode.calcscript.interpreter.builtins;

import biz.metacode.calcscript.interpreter.ExecutionContext;
import biz.metacode.calcscript.interpreter.Invocable;
import biz.metacode.calcscript.interpreter.OverloadMissingException;
import biz.metacode.calcscript.interpreter.Value;

/**
 * Allows overriding function names by dispatching call to function based on the
 * type of the top value on the stack.
 */
public class SingleDispatcher implements Invocable {

    private static final long serialVersionUID = -8695300280295378206L;

    private final String prefix;

    /**
     * Creates a single dispatcher for group of functions with given prefix.
     *
     * @param prefix Text prefix that this dispatcher should handle.
     */
    public SingleDispatcher(final String prefix) {
        this.prefix = prefix;
    }

    /**
     * Dispatches execution to sub-function based on parameter type.
     *
     * @param context Execution context.
     * @throws InterruptedException When script execution is interrupted.
     */
    public final void invoke(final ExecutionContext context) throws InterruptedException {
        Value first = context.pop();
        String methodName = prefix + "_" + first.getType();

        context.push(first);

        first.release();

        Invocable targetMethod = context.read(methodName);
        if (targetMethod != null) {
            targetMethod.invoke(context);
        } else {
            throw new OverloadMissingException(first.getType());
        }
    }

}
