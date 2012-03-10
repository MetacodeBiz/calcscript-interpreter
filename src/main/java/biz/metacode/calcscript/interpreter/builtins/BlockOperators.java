
package biz.metacode.calcscript.interpreter.builtins;

import biz.metacode.calcscript.interpreter.ExecutionContext;
import biz.metacode.calcscript.interpreter.Invocable;
import biz.metacode.calcscript.interpreter.SharedArray;
import biz.metacode.calcscript.interpreter.Value;
import biz.metacode.calcscript.interpreter.source.Program;

/**
 * Operators that work on blocks and functions.
 */
public enum BlockOperators implements Invocable {
    /**
     * Contatenate two blocks into one. Takes two blocks and leaves one on
     * stack.
     */
    CONCATENATE {

        public void invoke(ExecutionContext context) throws InterruptedException {
            final Value first = context.pop();
            final Value second = context.pop();
            try {
                context.push(Program.concatenate(second, first));
            } finally {
                second.release();
                first.release();
            }
        }

    },
    /**
     * Creates an array by executing blocks. Takes two blocks and leaves one
     * array on stack.
     */
    UNFOLD {
        public void invoke(final ExecutionContext context) throws InterruptedException {
            Invocable loop = context.pop();
            Invocable test = context.pop();
            SharedArray result = context.acquireArray();
            while (true) {
                context.interruptionPoint();
                context.push(context.peek().duplicate());
                test.invoke(context);
                boolean passed = context.popBoolean();
                if (passed) {
                    result.add(context.peek());
                } else {
                    break;
                }
                loop.invoke(context);
            }
            context.pop().release();
            context.pushArray(result);
        }
    },
    /**
     * Select elements from array that pass a test. Takes a block and an array
     * and leaves an array on stack.
     */
    FILTER {
        public void invoke(final ExecutionContext context) throws InterruptedException {
            Value filter = context.pop();
            SharedArray array = context.pop().asArray();
            try {
                SharedArray result = context.acquireArray();
                for (Value value : array) {
                    context.push(value);
                    filter.invoke(context);
                    if (context.popBoolean()) {
                        result.add(value);
                    }
                }
                context.pushArray(result);
            } finally {
                filter.release();
                array.release();
            }
        }
    },
    /**
     * Returns a first element that passes the test. Takes a block and an array
     * and leaves that element on stack or nothing if no elements pass the test.
     */
    FIND {
        public void invoke(final ExecutionContext context) throws InterruptedException {
            Value filter = context.pop();
            SharedArray array = context.pop().asArray();
            try {
                for (Value value : array) {
                    context.push(value);
                    filter.invoke(context);
                    if (context.popBoolean()) {
                        context.push(value);
                        return;
                    }
                }
            } finally {
                filter.release();
                array.release();
            }
        }
    },
    /**
     * Executes given block. Takes one block and does not leave any explicit
     * values.
     */
    EXECUTE {
        public void invoke(ExecutionContext context) throws InterruptedException {
            Value executable = context.pop();
            try {
                executable.invoke(context);
            } finally {
                executable.release();
            }
        }
    }
}
