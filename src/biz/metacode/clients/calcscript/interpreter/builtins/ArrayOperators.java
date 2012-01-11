
package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.ExecutionContext;
import biz.metacode.clients.calcscript.interpreter.Invocable;
import biz.metacode.clients.calcscript.interpreter.SharedArray;
import biz.metacode.clients.calcscript.interpreter.Value;

public enum ArrayOperators implements Invocable {
    MAP {
        @Override
        public void invoke(ExecutionContext context) {
            Invocable executable = (Invocable) context.pop();
            SharedArray list = (SharedArray) context.pop();
            SharedArray results = context.acquireArray();
            for (Value object : list) {
                context.push(object);
                executable.invoke(context);
                results.add(context.pop());
            }
            context.pushArray(results);
        }
    },
    COMMA {
        @Override
        public void invoke(ExecutionContext context) {
            SharedArray array = (SharedArray) context.pop();
            context.pushDouble(array.size());
            array.release();
        }
    },
}
