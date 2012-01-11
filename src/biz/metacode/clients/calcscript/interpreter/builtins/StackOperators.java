
package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.ExecutionContext;
import biz.metacode.clients.calcscript.interpreter.Invocable;
import biz.metacode.clients.calcscript.interpreter.Value;

public enum StackOperators implements Invocable {

    LEFT_SQUARE_BRACE {
        @Override
        public void invoke(ExecutionContext context) {
            context.markPosition();
        }
    },
    RIGHT_SQUARE_BRACE {
        @Override
        public void invoke(ExecutionContext context) {
            context.pushArray(context.extractMarkedArray());
        }
    },
    DUPLICATE {
        @Override
        public void invoke(ExecutionContext context) {
            context.push(context.peek().duplicate());
        }
    },
    DROP {
        @Override
        public void invoke(ExecutionContext context) {
            context.pop();
        }
    },
    SWAP {
        @Override
        public void invoke(ExecutionContext context) {
            Value first = context.pop();
            Value second = context.pop();
            context.push(first);
            context.push(second);
        }
    }
}
