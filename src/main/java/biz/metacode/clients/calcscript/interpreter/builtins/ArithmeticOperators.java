package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.ExecutionContext;
import biz.metacode.clients.calcscript.interpreter.Invocable;

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
    }
}
