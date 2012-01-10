
package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.Executable;
import biz.metacode.clients.calcscript.interpreter.execution.Array;
import biz.metacode.clients.calcscript.interpreter.execution.Context;
import biz.metacode.clients.calcscript.interpreter.execution.Value;

public enum MathOperators implements Executable {

    SUM {
        @Override
        public void execute(Context context) {
            Array list = (Array) context.pop();
            double sum = 0;
            for (Object o : list) {
                sum += ((Value) o).consume();
            }
            context.pushDouble(sum);
        }
    },
    ABSOLUTE {
        @Override
        public void execute(Context context) {
            context.pushDouble(Math.abs(context.popDouble()));
        }
    },
}
