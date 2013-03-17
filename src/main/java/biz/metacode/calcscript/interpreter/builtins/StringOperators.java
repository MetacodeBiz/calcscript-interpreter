
package biz.metacode.calcscript.interpreter.builtins;

import biz.metacode.calcscript.interpreter.ExecutionContext;
import biz.metacode.calcscript.interpreter.Invocable;
import biz.metacode.calcscript.interpreter.SharedArray;
import biz.metacode.calcscript.interpreter.Value;
import biz.metacode.calcscript.interpreter.source.Program;

import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * Operators that work on strings.
 */
public enum StringOperators implements Invocable {
    /**
     * Joins two strings together. Takes two strings and leaves one on stack.
     */
    CONCATENATE {

        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) throws InterruptedException {
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
    /**
     * Converts value to string. Takes one value and leaves one string on stack.
     */
    TO_STRING {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) throws InterruptedException {
            Value first = context.pop();
            try {
                context.pushString(first.toString());
            } finally {
                first.release();
            }
        }
    },
    /**
     * Sorts letters in string. Takes one string and leaves on string on stack.
     */
    SORT_LETTERS {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) throws InterruptedException {
            Value first = context.pop();
            try {
                char[] letters = first.toString().toCharArray();
                Arrays.sort(letters);
                context.pushString(String.valueOf(letters));
            } finally {
                first.release();
            }
        }
    },
    /**
     * Repeats string given number of times. Takes a string and a number and
     * leaves one string on stack.
     */
    REPEAT {
        /**
         * {@inheritDoc}
         */
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
    /**
     * Joins a string using another string as a separator. Takes two strings and
     * leaves one on the stack.
     */
    JOIN {
        /**
         * {@inheritDoc}
         */
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
    /**
     * Split string around matches made of another string omitting empty
     * results. Takes two strings and leaves one array on stack.
     */
    SPLIT_AROUND_MATCHES_NONEMPTY {
        /**
         * {@inheritDoc}
         */
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
    /**
     * Treats a string as a script code and interprets it. Takes one string and
     * leaves no explicit values on stack.
     */
    EVAL {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) throws InterruptedException {
            String code = context.popString();
            Program program = new Program(code);
            program.invoke(context);
        }
    }
}
