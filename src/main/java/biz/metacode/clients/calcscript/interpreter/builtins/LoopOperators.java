
package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.ExecutionContext;
import biz.metacode.clients.calcscript.interpreter.Invocable;
import biz.metacode.clients.calcscript.interpreter.Value;

public enum LoopOperators implements Invocable {
    DO {
        public void invoke(ExecutionContext context) throws InterruptedException {
            Invocable block = (Invocable) context.pop();
            Value value = null;
            do {
                if (value != null) {
                    value.release();
                }
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
                block.invoke(context);
                value = context.pop();
            } while (value.toBoolean());
        }
    },
    TIMES {
        public void invoke(ExecutionContext context) throws InterruptedException {
            Invocable block = (Invocable) context.pop();
            double times = context.popDouble();
            for (int i = (int) times - 1; i >= 0; i--) {
                block.invoke(context);
            }
        }
    },
}
