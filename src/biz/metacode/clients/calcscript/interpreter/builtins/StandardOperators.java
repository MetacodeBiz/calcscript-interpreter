
package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.Executable;
import biz.metacode.clients.calcscript.interpreter.Memory;
import biz.metacode.clients.calcscript.interpreter.Stack;

import java.util.List;

public enum StandardOperators implements Executable {

    ADDITION {
        @Override
        public void execute(Stack stack, Memory memory) {
            Double first = (Double) stack.pop();
            Double second = (Double) stack.pop();
            stack.push(first + second);
        }
    },
    SUBSTRACTION {
        @Override
        public void execute(Stack stack, Memory memory) {
            Double first = (Double) stack.pop();
            Double second = (Double) stack.pop();
            stack.push(first - second);
        }
    },
    MULTIPLICATION {
        @Override
        public void execute(Stack stack, Memory memory) {
            Double first = (Double) stack.pop();
            Double second = (Double) stack.pop();
            stack.push(first * second);
        }
    },
    DIVISION {
        @Override
        public void execute(Stack stack, Memory memory) {
            Double first = (Double) stack.pop();
            Double second = (Double) stack.pop();
            stack.push(first / second);
        }
    },
    LEFT_SQUARE_BRACE {
        @Override
        public void execute(Stack stack, Memory memory) {
            stack.markPosition();
        }
    },
    RIGHT_SQUARE_BRACE {
        @Override
        public void execute(Stack stack, Memory memory) {
            stack.push(stack.extractMarkedArray());
        }
    },
    SUM {
        @Override
        public void execute(Stack stack, Memory memory) {
            @SuppressWarnings("unchecked")
            List<Object> list = (List<Object>) stack.pop();
            double sum = 0;
            for (Object o : list) {
                sum += (Double) o;
            }
            stack.push(sum);
        }
    },
    SEMICOLON {

        @Override
        public void execute(Stack stack, Memory memory) {
            stack.pop();
        }

    }
}
