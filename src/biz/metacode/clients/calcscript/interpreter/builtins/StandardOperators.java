
package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.Executable;
import biz.metacode.clients.calcscript.interpreter.execution.Array;
import biz.metacode.clients.calcscript.interpreter.execution.Context;
import biz.metacode.clients.calcscript.interpreter.execution.Value;

public enum StandardOperators implements Executable {

    ADDITION {
        @Override
        public void execute(Context context) {
            double first = context.popDouble();
            double second = context.popDouble();
            context.pushDouble(first + second);
        }
    },
    SUBSTRACTION {
        @Override
        public void execute(Context context) {
            double first = context.popDouble();
            double second = context.popDouble();
            context.pushDouble(first - second);
        }
    },
    MULTIPLICATION {
        @Override
        public void execute(Context context) {
            double first = context.popDouble();
            double second = context.popDouble();
            context.pushDouble(first * second);
        }
    },
    DIVISION {
        @Override
        public void execute(Context context) {
            double first = context.popDouble();
            double second = context.popDouble();
            context.pushDouble(first / second);
        }
    },
    LEFT_SQUARE_BRACE {
        @Override
        public void execute(Context context) {
            context.markPosition();
        }
    },
    RIGHT_SQUARE_BRACE {
        @Override
        public void execute(Context context) {
            context.push(context.extractMarkedArray());
        }
    },
    SUM {
        @Override
        public void execute(Context context) {
            Array list = (Array) context.pop();
            double sum = 0;
            for (Object o : list) {
                sum += ((Value) o).consume();
            }
            context.pushDouble(sum);
        }
    },
    MAP {
        @Override
        public void execute(Context context) {
            Executable executable = (Executable) context.pop();
            Array list = (Array) context.pop();
            Array results = context.acquireArray();
            for (Object object : list) {
                context.push(object);
                executable.execute(context);
                results.add(context.pop());
            }
            context.push(results);
        }
    },
    SEMICOLON {
        @Override
        public void execute(Context context) {
            context.pop();
        }
    },
    COMMA {
        @Override
        public void execute(Context context) {
            Array array = (Array) context.pop();
            context.pushDouble(array.length());
            array.relinquish();
        }
    },
    ABSOLUTE {
        @Override
        public void execute(Context context) {
            context.pushDouble(Math.abs(context.popDouble()));
        }
    }
}
