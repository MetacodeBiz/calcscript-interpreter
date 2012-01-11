package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.Invocable;
import biz.metacode.clients.calcscript.interpreter.execution.Array;
import biz.metacode.clients.calcscript.interpreter.execution.Context;
import biz.metacode.clients.calcscript.interpreter.execution.Value;

public enum ArrayOperators implements Invocable {
    MAP {
        @Override
        public void invoke(Context context) {
            Invocable executable = (Invocable) context.pop();
            Array list = (Array) context.pop();
            Array results = context.acquireArray();
            for (Value object : list) {
                context.push(object);
                executable.invoke(context);
                results.add(context.pop());
            }
            context.push(results);
        }
    },
    COMMA {
        @Override
        public void invoke(Context context) {
            Array array = (Array) context.pop();
            context.push(array.length());
            array.relinquish();
        }
    },
}
