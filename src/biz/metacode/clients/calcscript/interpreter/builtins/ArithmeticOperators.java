package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.Executable;
import biz.metacode.clients.calcscript.interpreter.execution.Context;

public enum ArithmeticOperators implements Executable {
    ADDITION {
        @Override
        public void execute(Context context) {
            double first = context.popDouble();
            double second = context.popDouble();
            context.push(first + second);
        }
    },
    SUBSTRACTION {
        @Override
        public void execute(Context context) {
            double first = context.popDouble();
            double second = context.popDouble();
            context.push(first - second);
        }
    },
    MULTIPLICATION {
        @Override
        public void execute(Context context) {
            double first = context.popDouble();
            double second = context.popDouble();
            context.push(first * second);
        }
    },
    DIVISION {
        @Override
        public void execute(Context context) {
            double first = context.popDouble();
            double second = context.popDouble();
            context.push(first / second);
        }
    }
}
