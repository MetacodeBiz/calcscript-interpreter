
package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.ExecutionContext;
import biz.metacode.clients.calcscript.interpreter.Invocable;
import biz.metacode.clients.calcscript.interpreter.SharedArray;
import biz.metacode.clients.calcscript.interpreter.execution.Numeric;

public enum MathOperators implements Invocable {

    SUM {
        @Override
        public void invoke(ExecutionContext context) {
            SharedArray list = (SharedArray) context.pop();
            double sum = 0;
            for (Object o : list) {
                sum += ((Numeric) o).consume();
            }
            context.pushDouble(sum);
        }
    },
    ABSOLUTE {
        @Override
        public void invoke(ExecutionContext context) {
            context.pushDouble(Math.abs(context.popDouble()));
        }
    },
}
