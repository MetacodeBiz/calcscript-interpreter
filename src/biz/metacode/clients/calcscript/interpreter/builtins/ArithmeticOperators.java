package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.Invocable;
import biz.metacode.clients.calcscript.interpreter.execution.Context;

public enum ArithmeticOperators implements Invocable {
    ADDITION {
        @Override
        public void invoke(Context context) {
            double first = context.popDouble();
            double second = context.popDouble();
            context.push(first + second);
        }
    },
    SUBSTRACTION {
        @Override
        public void invoke(Context context) {
            double first = context.popDouble();
            double second = context.popDouble();
            context.push(first - second);
        }
    },
    MULTIPLICATION {
        @Override
        public void invoke(Context context) {
            double first = context.popDouble();
            double second = context.popDouble();
            context.push(first * second);
        }
    },
    DIVISION {
        @Override
        public void invoke(Context context) {
            double first = context.popDouble();
            double second = context.popDouble();
            context.push(first / second);
        }
    }
}
