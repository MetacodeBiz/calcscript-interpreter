
package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.ExecutionContext;
import biz.metacode.clients.calcscript.interpreter.Invocable;

public enum LoopOperators implements Invocable {
    DO {
        public void invoke(ExecutionContext context) throws InterruptedException {
            Invocable block = (Invocable) context.pop();
            double value = 0;
            do {
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
                block.invoke(context);
                value = context.popDouble();
            } while (value != 0);
        }
    }
}
