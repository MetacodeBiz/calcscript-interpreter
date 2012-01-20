
package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.ExecutionContext;
import biz.metacode.clients.calcscript.interpreter.Invocable;

import java.util.Random;

public enum ArithmeticOperators implements Invocable {
    ADDITION {
        public void invoke(ExecutionContext context) {
            double first = context.popDouble();
            double second = context.popDouble();
            context.pushDouble(second + first);
        }
    },
    SUBSTRACTION {
        public void invoke(ExecutionContext context) {
            double first = context.popDouble();
            double second = context.popDouble();
            context.pushDouble(second - first);
        }
    },
    MULTIPLICATION {
        public void invoke(ExecutionContext context) {
            double first = context.popDouble();
            double second = context.popDouble();
            context.pushDouble(second * first);
        }
    },
    DIVISION {
        public void invoke(ExecutionContext context) {
            double first = context.popDouble();
            double second = context.popDouble();
            context.pushDouble(second / first);
        }
    },
    MODULO {
        public void invoke(ExecutionContext context) {
            double first = context.popDouble();
            double second = context.popDouble();
            context.pushDouble(second % first);
        }
    },
    BITWISE_NEGATION {
        public void invoke(ExecutionContext context) {
            context.pushDouble(~(long) (context.popDouble()));
        }
    },
    POWER {
        public void invoke(ExecutionContext context) {
            double first = context.popDouble();
            double second = context.popDouble();
            context.pushDouble(Math.pow(second, first));
        }
    },
    DECREMENT {
        public void invoke(ExecutionContext context) {
            context.pushDouble(context.popDouble() - 1);
        }
    },
    INCREMENT {
        public void invoke(ExecutionContext context) {
            context.pushDouble(context.popDouble() + 1);
        }
    },
    RANDOM {
        public void invoke(ExecutionContext context) {
            context.pushDouble(new Random().nextInt((int) context.popDouble()));
        }
    }
}
