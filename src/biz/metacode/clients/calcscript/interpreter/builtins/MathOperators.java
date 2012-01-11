
package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.Invocable;
import biz.metacode.clients.calcscript.interpreter.execution.Array;
import biz.metacode.clients.calcscript.interpreter.execution.Context;
import biz.metacode.clients.calcscript.interpreter.execution.Numeric;

public enum MathOperators implements Invocable {

    SUM {
        @Override
        public void invoke(Context context) {
            Array list = (Array) context.pop();
            double sum = 0;
            for (Object o : list) {
                sum += ((Numeric) o).consume();
            }
            context.push(sum);
        }
    },
    ABSOLUTE {
        @Override
        public void invoke(Context context) {
            context.push(Math.abs(context.popDouble()));
        }
    },
}
