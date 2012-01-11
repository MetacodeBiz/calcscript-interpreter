package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.ExecutionContext;
import biz.metacode.clients.calcscript.interpreter.Invocable;

public enum ArithmeticOperators implements Invocable {
    ADDITION {
        @Override
        public void invoke(ExecutionContext context) {
            double first = context.popDouble();
            double second = context.popDouble();
            context.pushDouble(first + second);
        }
    },
    SUBSTRACTION {
        @Override
        public void invoke(ExecutionContext context) {
            double first = context.popDouble();
            double second = context.popDouble();
            context.pushDouble(first - second);
        }
    },
    MULTIPLICATION {
        @Override
        public void invoke(ExecutionContext context) {
            double first = context.popDouble();
            double second = context.popDouble();
            context.pushDouble(first * second);
        }
    },
    DIVISION {
        @Override
        public void invoke(ExecutionContext context) {
            double first = context.popDouble();
            double second = context.popDouble();
            context.pushDouble(first / second);
        }
    }
}
