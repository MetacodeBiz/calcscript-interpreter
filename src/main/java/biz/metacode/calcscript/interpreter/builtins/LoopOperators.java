
package biz.metacode.calcscript.interpreter.builtins;

import biz.metacode.calcscript.interpreter.ExecutionContext;
import biz.metacode.calcscript.interpreter.Invocable;
import biz.metacode.calcscript.interpreter.SelfDescribing;
import biz.metacode.calcscript.interpreter.Value;

/**
 * Operators that perform loops.
 */
public enum LoopOperators implements Invocable, SelfDescribing {
    /**
     * Executes block until it returns {@code false}. Takes one block and leaves
     * no explicit values.
     */
    DO {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) throws InterruptedException {
            Invocable block = context.pop();
            Value value = null;
            do {
                if (value != null) {
                    value.release();
                }
                context.interruptionPoint();
                block.invoke(context);
                value = context.pop();
            } while (value.toBoolean());
            value.release();
        }

        /**
         * {@inheritDoc}
         */
        public String getExampleUsage() {
            return "168 35 {.@\\%.} do;";
        }
    },
    /**
     * Executes a block given number of times. Takes a block and number and
     * leaves no explicit values on stack.
     */
    TIMES {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) throws InterruptedException {
            Invocable block = context.pop();
            double times = context.popDouble();
            for (int i = (int) times - 1; i >= 0; i--) {
                context.interruptionPoint();
                block.invoke(context);
            }
        }

        /**
         * {@inheritDoc}
         */
        public String getExampleUsage() {
            return "2 {2*} 5*";
        }
    },
    /**
     * Executes a body block until a test passes (returns {@code true}). Takes
     * two blocks and leaves no explicit values on stack.
     */
    WHILE {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) throws InterruptedException {
            Invocable body = context.pop();
            Invocable test = context.pop();
            do {
                context.interruptionPoint();
                test.invoke(context);
                if (!context.popBoolean()) {
                    break;
                }
                body.invoke(context);
            } while (true);
        }

        /**
         * {@inheritDoc}
         */
        public String getExampleUsage() {
            return "5{.}{1-.}while";
        }
    },
    /**
     * Executes a body block until a test fails (returns {@code false}). Takes
     * two blocks and leaves no explicit values on stack.
     */
    UNTIL {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) throws InterruptedException {
            Invocable body = context.pop();
            Invocable test = context.pop();
            do {
                context.interruptionPoint();
                test.invoke(context);
                if (context.popBoolean()) {
                    break;
                }
                body.invoke(context);
            } while (true);
        }

        /**
         * {@inheritDoc}
         */
        public String getExampleUsage() {
            return "5{.}{1-.}until";
        }
    }
}
