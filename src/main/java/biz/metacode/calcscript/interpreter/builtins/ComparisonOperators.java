
package biz.metacode.calcscript.interpreter.builtins;

import biz.metacode.calcscript.interpreter.ExecutionContext;
import biz.metacode.calcscript.interpreter.Invocable;
import biz.metacode.calcscript.interpreter.Value;

/**
 * Operators for comparing values.
 */
public enum ComparisonOperators implements Invocable {
    /**
     * Checks if one value is less than another. Takes two values and leaves one
     * number on stack.
     */
    LESS_THAN {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) throws InterruptedException {
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
    /**
     * Checks if one value is greater than another. Takes two values and leaves
     * one number on stack.
     */
    GREATER_THAN {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) throws InterruptedException {
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
    /**
     * Checks if one value is equal to another. Takes two values and leaves one
     * number on stack.
     */
    EQUALS {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) throws InterruptedException {
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
