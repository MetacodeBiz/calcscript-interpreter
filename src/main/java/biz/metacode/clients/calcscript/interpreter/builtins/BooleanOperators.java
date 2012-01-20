
package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.ExecutionContext;
import biz.metacode.clients.calcscript.interpreter.Invocable;
import biz.metacode.clients.calcscript.interpreter.Value;

public enum BooleanOperators implements Invocable {
    NOT {
        public void invoke(ExecutionContext context) throws InterruptedException {
            context.pushBoolean(!context.popBoolean());
        }
    },
    OR {
        public void invoke(ExecutionContext context) throws InterruptedException {
            Value first = context.pop();
            Value second = context.pop();
            try {
                second.invoke(context);
                if (!context.peek().toBoolean()) {
                    first.invoke(context);
                }
            } finally {
                second.release();
                first.release();
            }
        }
    },
    AND {
        public void invoke(ExecutionContext context) throws InterruptedException {
            Value first = context.pop();
            Value second = context.pop();
            try {
                second.invoke(context);
                if (context.peek().toBoolean()) {
                    context.pop().release();
                    first.invoke(context);
                }
            } finally {
                second.release();
                first.release();
            }
        }
    },
    XOR {
        public void invoke(ExecutionContext context) throws InterruptedException {
            Value first = context.pop();
            Value second = context.pop();
            try {
                second.invoke(context);
                Value secondRes = context.pop();
                first.invoke(context);
                Value firstRes = context.pop();

                try {
                    if (secondRes.toBoolean() ^ firstRes.toBoolean()) {
                        context.push(firstRes);
                    } else {
                        context.pushDouble(0);
                    }
                } finally {
                    secondRes.release();
                    firstRes.release();
                }
            } finally {
                second.release();
                first.release();
            }
        }
    },
    IF {
        public void invoke(ExecutionContext context) throws InterruptedException {
            Value first = context.pop();
            Value second = context.pop();
            Value third = context.pop();
            try {
                if (third.toBoolean()) {
                    second.invoke(context);
                } else {
                    first.invoke(context);
                }
            } finally {
                third.release();
                second.release();
                first.release();
            }
        }

    }
}
