
package biz.metacode.calcscript.interpreter.builtins;

import biz.metacode.calcscript.interpreter.ExecutionContext;
import biz.metacode.calcscript.interpreter.Invocable;
import biz.metacode.calcscript.interpreter.Value;

public class SingleDispatcher implements Invocable {

    private static final long serialVersionUID = -8695300280295378206L;

    private final String prefix;

    public SingleDispatcher(String prefix) {
        this.prefix = prefix;
    }

    public final void invoke(ExecutionContext context) throws InterruptedException {
        Value first = context.pop();
        String methodName = prefix + "_" + first.getTypeName();

        context.push(first);

        first.release();

        Invocable targetMethod = context.read(methodName);
        if (targetMethod != null) {
            targetMethod.invoke(context);
        } else {
            throw new ClassCastException("ERR!!");
        }
    }

}
