
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
    },
    REPEAT {
        public void invoke(final ExecutionContext context) throws InterruptedException {
            String first = context.popString();
            double times = context.popDouble();

            StringBuilder sb = new StringBuilder();
            for (int i = (int) times - 1; i >= 0; i--) {
                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
                sb.append(first);
            }
            context.pushString(sb.toString());
        }
    },
    JOIN {
        public void invoke(final ExecutionContext context) throws InterruptedException {
            String first = context.popString();
            String second = context.popString();

            StringBuilder sb = new StringBuilder();
            for (int i = 0, l = second.length(); i < l; i++) {
                sb.append(second.charAt(i));
                if (i != l - 1) {
                    sb.append(first);
                }
            }
            context.pushString(sb.toString());
        }
    }
}
