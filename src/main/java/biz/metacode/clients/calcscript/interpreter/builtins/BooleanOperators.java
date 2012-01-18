
package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.ExecutionContext;
import biz.metacode.clients.calcscript.interpreter.Invocable;
import biz.metacode.clients.calcscript.interpreter.Value;

public enum BooleanOperators implements Invocable {
    NOT {
        public void invoke(ExecutionContext context) throws InterruptedException {
            Value value = context.pop();
            try {
                context.pushDouble(value.toBoolean() ? 0 : 1);
            } finally {
                value.release();
            }
        }
    }
}
