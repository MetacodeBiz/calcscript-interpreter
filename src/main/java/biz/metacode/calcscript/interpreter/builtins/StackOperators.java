
package biz.metacode.calcscript.interpreter.builtins;

import biz.metacode.calcscript.interpreter.ExecutionContext;
import biz.metacode.calcscript.interpreter.Invocable;
import biz.metacode.calcscript.interpreter.ScriptExecutionException;
import biz.metacode.calcscript.interpreter.Value;
import biz.metacode.calcscript.interpreter.ValueMissingException;

/**
 * Operators that work on the stack.
 */
public enum StackOperators implements Invocable {
    /**
     * Marks current position of the stack for slicing. Does not take or leave
     * values on stack.
     */
    MARK_STACK_SIZE {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) {
            context.markPosition();
        }
    },
    /**
     * Slices the stack where it was previously marked or at the bottom if no
     * marks have been made. Does not take arguments but leaves one array on
     * stack.
     */
    SLICE_STACK {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) {
            context.pushArray(context.extractMarkedArray());
        }
    },
    /**
     * Duplicates the element on top of the stack. Looks up one argument and
     * leaves a copy of it one the stack.
     */
    DUPLICATE {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) {
            context.push(context.peek().duplicate());
        }
    },
    /**
     * Discards the top stack value. Takes on argument and leaves nothing on
     * stack.
     */
    DROP {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) {
            context.pop().release();
        }
    },
    /**
     * Swaps two top-most values on the stack. Takes two arguments and leaves
     * two values on stack.
     */
    SWAP {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) {
            Value first = context.pop();
            Value second = context.pop();
            context.push(first);
            context.push(second);
            first.release();
            second.release();
        }
    },
    /**
     * Rotates three top-most values on the stack. Takes three arguments and
     * leaves three values on stack.
     */
    ROT3 {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) {
            try {
                Value first = context.pop();
                Value second = context.pop();
                Value third = context.pop();
                context.push(second);
                context.push(first);
                context.push(third);
                first.release();
                second.release();
                third.release();
            } catch (ValueMissingException e) {
                throw new ScriptExecutionException("@", "1 2 3<name>", e);
            }
        }
    },
    /**
     * Returns n-th element on the stack. Takes on number (stack index) and
     * leaves one value on the stack.
     */
    GET_NTH {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) {
            Value value = context.peekAt((int) context.popDouble());
            context.push(value);
        }
    }
}
