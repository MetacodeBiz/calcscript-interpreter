
package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.ExecutionContext;
import biz.metacode.clients.calcscript.interpreter.Invocable;
import biz.metacode.clients.calcscript.interpreter.Value;

public class SingleDispatcher implements Invocable {

    private static final long serialVersionUID = -8695300280295378206L;

    private final String prefix;

    public SingleDispatcher(String prefix) {
        this.prefix = prefix;
    }

    public final void invoke(ExecutionContext context) throws InterruptedException {

        Value first = context.pop();
        try {
            String methodName = prefix + "_" + first.getTypeName();

            context.push(first);

            Invocable targetMethod = context.read(methodName);
            if (targetMethod != null) {
                targetMethod.invoke(context);
            } else {
                throw new ClassCastException("ERR!!");
            }
        } finally {
            first.release();
        }
    }

}
