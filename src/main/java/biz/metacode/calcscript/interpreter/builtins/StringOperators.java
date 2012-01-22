
package biz.metacode.calcscript.interpreter.builtins;

import biz.metacode.calcscript.interpreter.ExecutionContext;
import biz.metacode.calcscript.interpreter.Invocable;
import biz.metacode.calcscript.interpreter.Program;
import biz.metacode.calcscript.interpreter.SharedArray;
import biz.metacode.calcscript.interpreter.Value;

import java.util.Arrays;
import java.util.StringTokenizer;

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
    },
    SPLIT_AROUND_MATCHES_NONEMPTY {
        public void invoke(final ExecutionContext context) throws InterruptedException {
            String delim = context.popString();
            String text = context.popString();
            StringTokenizer tokenizer = new StringTokenizer(text, delim);
            SharedArray result = context.acquireArray();
            while (tokenizer.hasMoreTokens()) {
                result.add(context.convertToValue(tokenizer.nextToken()));
            }
            context.pushArray(result);
        }
    },
    EVAL {
        public void invoke(ExecutionContext context) throws InterruptedException {
            String code = context.popString();
            Program program = new Program(code);
            program.invoke(context);
        }
    }
}
