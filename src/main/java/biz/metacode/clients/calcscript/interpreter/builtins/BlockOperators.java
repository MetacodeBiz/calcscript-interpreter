
package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.Block;
import biz.metacode.clients.calcscript.interpreter.ExecutionContext;
import biz.metacode.clients.calcscript.interpreter.Invocable;
import biz.metacode.clients.calcscript.interpreter.SharedArray;
import biz.metacode.clients.calcscript.interpreter.Value;

public enum BlockOperators implements Invocable {
    CONCATENATE {

        public void invoke(ExecutionContext context) throws InterruptedException {
            final Block first = (Block) context.pop();
            final Block second = (Block) context.pop();
            try {
                context.push(second.concatenate(first));
            } finally {
                second.release();
                first.release();
            }
        }

    },
    UNFOLD {
        public void invoke(final ExecutionContext context) throws InterruptedException {
            Invocable loop = context.pop();
            Invocable test = context.pop();
            SharedArray result = context.acquireArray();
            while (true) {
                context.interruptionPoint();
                context.push(context.peek().duplicate());
                test.invoke(context);
                boolean passed = context.popBoolean();
                if (passed) {
                    result.add(context.peek());
                } else {
                    break;
                }
                loop.invoke(context);
            }
            context.pop().release();
            context.pushArray(result);
        }
    },
    FILTER {
        public void invoke(final ExecutionContext context) throws InterruptedException {
            Invocable filter = context.pop();
            SharedArray array = (SharedArray) context.pop();
            try {
                SharedArray result = context.acquireArray();
                for (Value value : array) {
                    context.push(value);
                    filter.invoke(context);
                    if (context.popBoolean()) {
                        result.add(value);
                    }
                }
                context.pushArray(result);
            } finally {
                array.release();
            }
        }
    }
}
