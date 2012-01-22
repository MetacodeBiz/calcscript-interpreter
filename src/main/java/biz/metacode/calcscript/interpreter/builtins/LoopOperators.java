
package biz.metacode.calcscript.interpreter.builtins;

import biz.metacode.calcscript.interpreter.ExecutionContext;
import biz.metacode.calcscript.interpreter.Invocable;
import biz.metacode.calcscript.interpreter.Value;

public enum LoopOperators implements Invocable {
    DO {
        public void invoke(ExecutionContext context) throws InterruptedException {
            Invocable block = (Invocable) context.pop();
            Value value = null;
            do {
                if (value != null) {
                    value.release();
                }
                context.interruptionPoint();
                block.invoke(context);
                value = context.pop();
            } while (value.toBoolean());
            value.release();
        }
    },
    TIMES {
        public void invoke(ExecutionContext context) throws InterruptedException {
            Invocable block = (Invocable) context.pop();
            double times = context.popDouble();
            for (int i = (int) times - 1; i >= 0; i--) {
                context.interruptionPoint();
                block.invoke(context);
            }
        }
    },
    WHILE {
        public void invoke(ExecutionContext context) throws InterruptedException {
            Invocable body = (Invocable) context.pop();
            Invocable test = (Invocable) context.pop();
            do {
                context.interruptionPoint();
                test.invoke(context);
                if (!context.popBoolean()) {
                    break;
                }
                body.invoke(context);
            } while (true);
        }
    },
    UNTIL {
        public void invoke(ExecutionContext context) throws InterruptedException {
            Invocable body = (Invocable) context.pop();
            Invocable test = (Invocable) context.pop();
            do {
                context.interruptionPoint();
                test.invoke(context);
                if (context.popBoolean()) {
                    break;
                }
                body.invoke(context);
            } while (true);
        }
    }
}
