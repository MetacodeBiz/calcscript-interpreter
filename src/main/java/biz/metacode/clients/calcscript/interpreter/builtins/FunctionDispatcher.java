
package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.ExecutionContext;
import biz.metacode.clients.calcscript.interpreter.Invocable;
import biz.metacode.clients.calcscript.interpreter.Value;
import biz.metacode.clients.calcscript.interpreter.Value.Pair;

public abstract class FunctionDispatcher implements Invocable {

    private static final long serialVersionUID = -1053849869084680144L;

    private final String prefix;

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
            throw new ClassCastException("ERR!!");
        }
    }

    protected abstract Pair transform(ExecutionContext context, Value first, Value second);

}
