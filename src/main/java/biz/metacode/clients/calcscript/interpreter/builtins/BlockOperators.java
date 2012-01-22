
package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.ExecutionContext;
import biz.metacode.clients.calcscript.interpreter.Invocable;
import biz.metacode.clients.calcscript.interpreter.Program;
import biz.metacode.clients.calcscript.interpreter.SharedArray;
import biz.metacode.clients.calcscript.interpreter.Value;

public enum BlockOperators implements Invocable {
    CONCATENATE {

        public void invoke(ExecutionContext context) throws InterruptedException {
            final Value first = context.pop();
            final Value second = context.pop();
            try {
                context.push(Program.concatenate(second, first));
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
    },
    FIND {
        public void invoke(final ExecutionContext context) throws InterruptedException {
            Invocable filter = context.pop();
            SharedArray array = (SharedArray) context.pop();
            try {
                for (Value value : array) {
                    context.push(value);
                    filter.invoke(context);
                    if (context.popBoolean()) {
                        context.push(value);
                        return;
                    }
                }
            } finally {
                array.release();
            }
        }
    },
    EXECUTE {
        public void invoke(ExecutionContext context) throws InterruptedException {
            Value executable = context.pop();
            try {
                executable.invoke(context);
            } finally {
                executable.release();
            }
        }}
}
