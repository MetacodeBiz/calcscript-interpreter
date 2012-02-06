
package biz.metacode.calcscript.interpreter.builtins;

import biz.metacode.calcscript.interpreter.ExecutionContext;
import biz.metacode.calcscript.interpreter.Invocable;
import biz.metacode.calcscript.interpreter.SelfDescribing;
import biz.metacode.calcscript.interpreter.SharedArray;
import biz.metacode.calcscript.interpreter.Value;

public enum MathOperators implements Invocable, SelfDescribing {

    SUM {
        public void invoke(ExecutionContext context) {
            SharedArray list = context.pop().asArray();
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

        public String getExampleUsage() {
            return "[1 2 3]<name>";
        }
    },
    ABSOLUTE {
        public void invoke(ExecutionContext context) {
            context.pushDouble(Math.abs(context.popDouble()));
        }

        public String getExampleUsage() {
            return "-3<name>";
        }
    },
}
