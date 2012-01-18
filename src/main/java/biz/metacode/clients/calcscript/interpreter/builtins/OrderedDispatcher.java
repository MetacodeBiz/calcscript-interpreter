
package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.ExecutionContext;
import biz.metacode.clients.calcscript.interpreter.Invocable;
import biz.metacode.clients.calcscript.interpreter.Value;
import biz.metacode.clients.calcscript.interpreter.Value.Pair;

public class OrderedDispatcher implements Invocable {

    private static final long serialVersionUID = 5670005527311115309L;

    private final String prefix;

    public OrderedDispatcher(String prefix) {
        this.prefix = prefix;
    }

    public void invoke(ExecutionContext context) throws InterruptedException {
        Value first = context.pop();
        Value second = context.pop();
        try {
            Pair ordered = first.order(second);

            String methodName = prefix + "_" + ordered.first.getTypeName() + "_"
                    + ordered.second.getTypeName();

            context.push(second);
            context.push(first);

            Invocable targetMethod = context.read(methodName);
            if (targetMethod != null) {
                targetMethod.invoke(context);
            } else {
                throw new ClassCastException("ERR!!");
            }
        } finally {
            second.release();
            first.release();
        }
    }

}
