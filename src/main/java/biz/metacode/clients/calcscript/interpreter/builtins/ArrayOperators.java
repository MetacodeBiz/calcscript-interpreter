
package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.ExecutionContext;
import biz.metacode.clients.calcscript.interpreter.Invocable;
import biz.metacode.clients.calcscript.interpreter.SharedArray;
import biz.metacode.clients.calcscript.interpreter.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum ArrayOperators implements Invocable {
    MAP {
        public void invoke(ExecutionContext context) throws InterruptedException {
            Invocable executable = (Invocable) context.pop();
            SharedArray list = (SharedArray) context.pop();
            context.markPosition();
            try {
                for (Value object : list) {
                    context.push(object);
                    executable.invoke(context);
                }
            } finally {
                list.release();
            }
            context.pushArray(context.extractMarkedArray());
        }
    },
    COMMA {
        public void invoke(ExecutionContext context) {
            SharedArray array = (SharedArray) context.pop();
            context.pushDouble(array.size());
            array.release();
        }
    },
    EXTRACT {
        public void invoke(ExecutionContext context) {
            SharedArray array = (SharedArray) context.pop();
            try {
                for (Value value : array) {
                    context.push(value);
                }
            } finally {
                array.release();
            }
        }
    },
    SORT {
        public void invoke(ExecutionContext context) throws InterruptedException {
            SharedArray array = (SharedArray) context.pop();
            try {
                List<Value> values = new ArrayList<Value>(array);
                Collections.sort(values);
                SharedArray sorted = context.acquireArray();
                sorted.addAll(values);
                context.pushArray(sorted);
            } finally {
                array.release();
            }
        }
    }
}
