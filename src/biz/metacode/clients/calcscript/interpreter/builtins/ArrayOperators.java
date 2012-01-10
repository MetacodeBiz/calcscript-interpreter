package biz.metacode.clients.calcscript.interpreter.builtins;

import java.io.Serializable;

import biz.metacode.clients.calcscript.interpreter.Executable;
import biz.metacode.clients.calcscript.interpreter.execution.Array;
import biz.metacode.clients.calcscript.interpreter.execution.Context;

public enum ArrayOperators implements Executable {
    MAP {
        @Override
        public void execute(Context context) {
            Executable executable = (Executable) context.pop();
            Array list = (Array) context.pop();
            Array results = context.acquireArray();
            for (Serializable object : list) {
                context.push(object);
                executable.execute(context);
                results.add(context.pop());
            }
            context.push(results);
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
}
