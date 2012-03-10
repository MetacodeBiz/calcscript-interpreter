
package biz.metacode.calcscript.interpreter.builtins;

import biz.metacode.calcscript.interpreter.ExecutionContext;
import biz.metacode.calcscript.interpreter.Invocable;

/**
 * Operators that perform bitwise operations.
 */
public enum BitOperators implements Invocable {
    /**
     * Bitwise AND operation, treats numbers as {@code long}s. Takes two numbers
     * and leaves one number on stack.
     */
    AND {
        public void invoke(ExecutionContext context) throws InterruptedException {
            long first = (long) context.popDouble();
            long second = (long) context.popDouble();
            context.pushDouble(first & second);
        }
    },
    /**
     * Bitwise OR operation, treats numbers as {@code long}s. Takes two numbers
     * and leaves one number on stack.
     */
    OR {
        public void invoke(ExecutionContext context) throws InterruptedException {
            long first = (long) context.popDouble();
            long second = (long) context.popDouble();
            context.pushDouble(first | second);
        }
    },
    /**
     * Bitwise XOR operation, treats numbers as {@code long}s. Takes two numbers
     * and leaves one number on stack.
     */
    XOR {
        public void invoke(ExecutionContext context) throws InterruptedException {
            long first = (long) context.popDouble();
            long second = (long) context.popDouble();
            context.pushDouble(first ^ second);
        }
    },
    /**
     * Bitwise NOT operation, treats number as {@code long}s. Takes one number
     * and leaves one number on stack.
     */
    NOT {
        public void invoke(ExecutionContext context) {
            context.pushDouble(~(long) (context.popDouble()));
        }
    }
}
