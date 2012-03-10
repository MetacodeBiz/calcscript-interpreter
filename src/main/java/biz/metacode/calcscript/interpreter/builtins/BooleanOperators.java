
package biz.metacode.calcscript.interpreter.builtins;

import biz.metacode.calcscript.interpreter.ExecutionContext;
import biz.metacode.calcscript.interpreter.Invocable;
import biz.metacode.calcscript.interpreter.SelfDescribing;
import biz.metacode.calcscript.interpreter.Value;

/**
 * Operators that treat values as booleans. {@code 0}, {@code []}, {@code ""}
 * and {@code {@literal are treated as {@code false} and everything else as
 * {@code true}.
 */
public enum BooleanOperators implements Invocable, SelfDescribing {
    /**
     * Negates given value. Takes one value and leaves one number on stack.
     */
    NOT {
        public void invoke(ExecutionContext context) throws InterruptedException {
            context.pushBoolean(!context.popBoolean());
        }
    },
    /**
     * Lazy boolean OR - executes one block and if it returns false executes
     * another block. Takes two blocks and leaves no explicit values.
     */
    OR {
        public void invoke(ExecutionContext context) throws InterruptedException {
            Value first = context.pop();
            Value second = context.pop();
            try {
                second.invoke(context);
                if (!context.peek().toBoolean()) {
                    context.pop().release();
                    first.invoke(context);
                }
            } finally {
                second.release();
                first.release();
            }
        }
    },
    /**
     * Lazy boolean AND - executes one block and if it returns true executes
     * another block. Takes two blocks and leaves no explicit values.
     */
    AND {
        public void invoke(ExecutionContext context) throws InterruptedException {
            Value first = context.pop();
            Value second = context.pop();
            try {
                second.invoke(context);
                if (context.peek().toBoolean()) {
                    context.pop().release();
                    first.invoke(context);
                }
            } finally {
                second.release();
                first.release();
            }
        }
    },
    /**
     * Lazy boolean XOR - executes two blocks and leaves on stack {@code 1}
     * (truth) if both values are different or {@code 0} (false) if they are
     * equal. Takes two values and leaves no explicit values.
     */
    XOR {
        public void invoke(ExecutionContext context) throws InterruptedException {
            Value first = context.pop();
            Value second = context.pop();
            try {
                second.invoke(context);
                Value secondRes = context.pop();
                first.invoke(context);
                Value firstRes = context.pop();

                try {
                    if (secondRes.toBoolean() ^ firstRes.toBoolean()) {
                        context.push(firstRes);
                    } else {
                        context.pushDouble(0);
                    }
                } finally {
                    secondRes.release();
                    firstRes.release();
                }
            } finally {
                second.release();
                first.release();
            }
        }
    },
    /**
     * Executes a test and if it is true invokes first block or second if the
     * test is false. Takes three blocks and leaves no explicit values.
     */
    IF {
        public void invoke(ExecutionContext context) throws InterruptedException {
            Value first = context.pop();
            Value second = context.pop();
            Value third = context.pop();
            try {
                if (third.toBoolean()) {
                    second.invoke(context);
                } else {
                    first.invoke(context);
                }
            } finally {
                third.release();
                second.release();
                first.release();
            }
        }

        public String getExampleUsage() {
            return "0 2 {1.}<name>";
        }

    };

    public String getExampleUsage() {
        return null;
    }
}
