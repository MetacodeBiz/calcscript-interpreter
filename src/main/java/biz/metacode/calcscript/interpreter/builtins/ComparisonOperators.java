
package biz.metacode.calcscript.interpreter.builtins;

import biz.metacode.calcscript.interpreter.ExecutionContext;
import biz.metacode.calcscript.interpreter.Invocable;
import biz.metacode.calcscript.interpreter.Value;

public enum ComparisonOperators implements Invocable {
    LESS_THAN {
        public void invoke(ExecutionContext context) throws InterruptedException {
            Value first = context.pop();
            Value second = context.pop();
            try {
                context.pushBoolean(second.compareTo(first) < 0);
            } finally {
                second.release();
                first.release();
            }
        }
    },
    GREATER_THAN {
        public void invoke(ExecutionContext context) throws InterruptedException {
            Value first = context.pop();
            Value second = context.pop();
            try {
                context.pushBoolean(second.compareTo(first) > 0);
            } finally {
                second.release();
                first.release();
            }
        }
    },
    EQUALS {
        public void invoke(ExecutionContext context) throws InterruptedException {
            Value first = context.pop();
            Value second = context.pop();
            try {
                context.pushBoolean(second.equals(first));
            } finally {
                second.release();
                first.release();
            }
        }
    }
}
