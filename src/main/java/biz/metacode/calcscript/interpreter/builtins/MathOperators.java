
package biz.metacode.calcscript.interpreter.builtins;

import biz.metacode.calcscript.interpreter.ExecutionContext;
import biz.metacode.calcscript.interpreter.Invocable;
import biz.metacode.calcscript.interpreter.SelfDescribing;
import biz.metacode.calcscript.interpreter.SharedArray;
import biz.metacode.calcscript.interpreter.Value;

import java.util.LinkedList;

/**
 * Mathematical operators.
 */
public enum MathOperators implements Invocable, SelfDescribing {

    /**
     * Sums all numbers in the array. Takes an array and leaves one number on
     * stack.
     */
    SUM {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) {
            SharedArray list = context.pop().asArray();
            double sum = 0;
            try {
                for (Value value : list) {
                    sum += value.toDouble();
                }
            } finally {
                list.release();
            }
            context.pushDouble(sum);
        }

        /**
         * {@inheritDoc}
         */
        public String getExampleUsage() {
            return "[1 2 3]<name>";
        }
    },
    /**
     * Returns an absolute value of given number. Takes one number and leaves on
     * number on stack.
     */
    ABSOLUTE {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) {
            context.pushDouble(Math.abs(context.popDouble()));
        }

        /**
         * {@inheritDoc}
         */
        public String getExampleUsage() {
            return "-3<name>";
        }
    },
    /**
     * Converts number to base n represented as an array of numbers.
     */
    CONVERT_NUMBER_BASE {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) {
            int base = (int) Math.abs(context.popDouble());
            int number = (int) context.popDouble();
            // need a list here to insert at the beginning
            LinkedList<Value> results = new LinkedList<Value>();
            if (base == 1) {
                for (int i = 0; i < number; i++) {
                    results.add(context.convertToValue(1));
                }
            } else {
                while (number > 0) {
                    results.add(0, context.convertToValue(number % base));
                    number /= base;
                }
            }
            context.pushArray(results);
        }

        /**
         * {@inheritDoc}
         */
        public String getExampleUsage() {
            return "6 2<name>";
        }
    },
    /**
     * Converts number represented as an array to one number.
     */
    CONVERT_ARRAY_BASE {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) {
            SharedArray number = context.pop().asArray();
            double base = context.popDouble();
            try {
                if (base == 1) {
                    context.pushDouble(number.size());
                    return;
                }
                double converted = 0;
                int index = 0;
                int size = number.size();
                for (Value value : number) {
                    converted += value.toDouble() * Math.pow(base, size - index - 1);
                    index++;
                }
                context.pushDouble(converted);

            } finally {
                number.release();
            }
        }

        /**
         * {@inheritDoc}
         */
        public String getExampleUsage() {
            return "[1 1 0] 2<name>";
        }
    },
}
