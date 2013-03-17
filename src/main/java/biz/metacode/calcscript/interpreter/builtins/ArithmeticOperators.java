
package biz.metacode.calcscript.interpreter.builtins;

import biz.metacode.calcscript.interpreter.ExecutionContext;
import biz.metacode.calcscript.interpreter.Invocable;

/**
 * Mathematical operators that work on numbers.
 */
public enum ArithmeticOperators implements Invocable {
    /**
     * Arithmetic addition. Takes two numbers from the stack, leaves one.
     */
    ADDITION {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) {
            double first = context.popDouble();
            double second = context.popDouble();
            context.pushDouble(second + first);
        }
    },
    /**
     * Arithmetic substraction. Takes two numbers from the stack, leaves one.
     */
    SUBSTRACTION {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) {
            double first = context.popDouble();
            double second = context.popDouble();
            context.pushDouble(second - first);
        }
    },
    /**
     * Arithmetic multiplication. Takes two numbers from the stack, leaves one.
     */
    MULTIPLICATION {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) {
            double first = context.popDouble();
            double second = context.popDouble();
            context.pushDouble(second * first);
        }
    },
    /**
     * Arithmetic division. Takes two numbers from the stack, leaves one.
     */
    DIVISION {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) {
            double first = context.popDouble();
            double second = context.popDouble();
            context.pushDouble(second / first);
        }
    },
    /**
     * Arithmetic modulo division. Takes two numbers from the stack, leaves one.
     */
    MODULO {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) {
            double first = context.popDouble();
            double second = context.popDouble();
            context.pushDouble(second % first);
        }
    },
    /**
     * Arithmetic power. Takes two numbers from the stack, leaves one.
     */
    POWER {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) {
            double first = context.popDouble();
            double second = context.popDouble();
            context.pushDouble(Math.pow(second, first));
        }
    },
    /**
     * Number decrement. Takes one number from the stack, leaves one.
     */
    DECREMENT {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) {
            context.pushDouble(context.popDouble() - 1);
        }
    },
    /**
     * Number increment. Takes one number from the stack, leaves one.
     */
    INCREMENT {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) {
            context.pushDouble(context.popDouble() + 1);
        }
    },
    /**
     * Sinus function. Takes one number from the stack, leaves one.
     */
    SINUS {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) {
            context.pushDouble(Math.sin(context.popDouble()));
        }
    },
    /**
     * Cosinus function. Takes one number from the stack, leaves one.
     */
    COSINUS {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) {
            context.pushDouble(Math.cos(context.popDouble()));
        }
    },
    /**
     * Random function. Takes one number from the stack, leaves one.
     */
    RANDOM {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) {
            int limit = (int) context.popDouble();
            double random;
            if (limit < 1 && limit > -1) {
                random = Math.random();
            } else {
                boolean sign = limit < 0;
                limit = Math.abs(limit);
                random = Math.floor(Math.random() * limit);
                if (sign) {
                    random *= -1;
                }
            }
            context.pushDouble(random);
        }
    }
}
