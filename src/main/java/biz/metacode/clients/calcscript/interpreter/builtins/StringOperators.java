
package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.ExecutionContext;
import biz.metacode.clients.calcscript.interpreter.Invocable;
import biz.metacode.clients.calcscript.interpreter.Value;

import java.util.Arrays;

public enum StringOperators implements Invocable {
    CONCATENATE {

        public void invoke(ExecutionContext context) throws InterruptedException {
            Value first = context.pop();
            Value second = context.pop();
            try {
                context.pushString(second.toString() + first.toString());
            } finally {
                second.release();
                first.release();
            }
        }

    },
    TO_STRING {
        public void invoke(ExecutionContext context) throws InterruptedException {
            Value first = context.pop();
            try {
                context.pushString(first.toString());
            } finally {
                first.release();
            }
        }
    },
    SORT_LETTERS {
        public void invoke(ExecutionContext context) throws InterruptedException {
            Value first = context.pop();
            try {
                char[] letters = first.toString().toCharArray();
                Arrays.sort(letters);
                context.pushString(new String(letters));
            } finally {
                first.release();
            }
        }
    }
}
