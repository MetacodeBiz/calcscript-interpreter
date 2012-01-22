
package biz.metacode.calcscript.interpreter.builtins;

import biz.metacode.calcscript.interpreter.ExecutionContext;
import biz.metacode.calcscript.interpreter.Invocable;

public enum BitOperators implements Invocable {
    AND {
        public void invoke(ExecutionContext context) throws InterruptedException {
            long first = (long) context.popDouble();
            long second = (long) context.popDouble();
            context.pushDouble(first & second);
        }
    },
    OR {
        public void invoke(ExecutionContext context) throws InterruptedException {
            long first = (long) context.popDouble();
            long second = (long) context.popDouble();
            context.pushDouble(first | second);
        }
    },
    XOR {
        public void invoke(ExecutionContext context) throws InterruptedException {
            long first = (long) context.popDouble();
            long second = (long) context.popDouble();
            context.pushDouble(first ^ second);
        }
    },
    NOT {
        public void invoke(ExecutionContext context) {
            context.pushDouble(~(long) (context.popDouble()));
        }
    }
}
