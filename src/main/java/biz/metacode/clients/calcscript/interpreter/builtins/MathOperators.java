
package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.ExecutionContext;
import biz.metacode.clients.calcscript.interpreter.Invocable;
import biz.metacode.clients.calcscript.interpreter.SharedArray;
import biz.metacode.clients.calcscript.interpreter.Value;

public enum MathOperators implements Invocable {

    SUM {
        public void invoke(ExecutionContext context) {
            SharedArray list = (SharedArray) context.pop();
            double sum = 0;
            try {
                for (Value value : list) {
                    sum += value.toDouble();
                }
            } finally {
                list.release();
            }
            context.pushDouble(sum);
        }
    },
    ABSOLUTE {
        public void invoke(ExecutionContext context) {
            context.pushDouble(Math.abs(context.popDouble()));
        }
    },
}
