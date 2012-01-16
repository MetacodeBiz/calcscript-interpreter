
package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.ExecutionContext;
import biz.metacode.clients.calcscript.interpreter.Invocable;
import biz.metacode.clients.calcscript.interpreter.Value;

public enum StackOperators implements Invocable {

    LEFT_SQUARE_BRACE {
        public void invoke(ExecutionContext context) {
            context.markPosition();
        }
    },
    RIGHT_SQUARE_BRACE {
        public void invoke(ExecutionContext context) {
            context.pushArray(context.extractMarkedArray());
        }
    },
    DUPLICATE {
        public void invoke(ExecutionContext context) {
            context.push(context.peek().duplicate());
        }
    },
    DROP {
        public void invoke(ExecutionContext context) {
            context.pop().release();
        }
    },
    SWAP {
        public void invoke(ExecutionContext context) {
            Value first = context.pop();
            Value second = context.pop();
            context.push(first);
            context.push(second);
            first.release();
            second.release();
        }
    },
    ROT3 {
        public void invoke(ExecutionContext context) {
            Value first = context.pop();
            Value second = context.pop();
            Value third = context.pop();
            context.push(second);
            context.push(first);
            context.push(third);
            first.release();
            second.release();
            third.release();
        }
    }
}
