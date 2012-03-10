
package biz.metacode.calcscript.interpreter.builtins;

import java.util.LinkedList;

import biz.metacode.calcscript.interpreter.ExecutionContext;
import biz.metacode.calcscript.interpreter.Invocable;
import biz.metacode.calcscript.interpreter.SelfDescribing;
import biz.metacode.calcscript.interpreter.SharedArray;
import biz.metacode.calcscript.interpreter.Value;

/**
 * Mathematical operators.
 */
public enum MathOperators implements Invocable, SelfDescribing {

    /**
     * Sums all numbers in the array. Takes an array and leaves one number on
     * stack.
     */
    SUM {
        public void invoke(ExecutionContext context) {
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

        public String getExampleUsage() {
            return "[1 2 3]<name>";
        }
    },
    /**
     * Returns an absolute value of given number. Takes one number and leaves on
     * number on stack.
     */
    ABSOLUTE {
        public void invoke(ExecutionContext context) {
            context.pushDouble(Math.abs(context.popDouble()));
        }

        public String getExampleUsage() {
            return "-3<name>";
        }
    },
    CONVERT_NUMBER_BASE {
        public void invoke(ExecutionContext context) {
            int base = (int) context.popDouble();
            int number = (int) context.popDouble();
            // need a list here to insert at the beginning
            LinkedList<Value> results = new LinkedList<Value>();
            while(number > 0)
            {
                 results.add(0, context.convertToValue(number % base));
                 number /= base;
            }
            context.pushArray(results);
        }

        public String getExampleUsage() {
            return "6 2<name>";
        }
    },
    CONVERT_ARRAY_BASE {
        public void invoke(ExecutionContext context) {
            SharedArray number = context.pop().asArray();
            double base = context.popDouble();
            try {
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

        public String getExampleUsage() {
            return "[1 1 0] 2<name>";
        }
    },
}
