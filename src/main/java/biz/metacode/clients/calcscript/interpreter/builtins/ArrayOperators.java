
package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.ExecutionContext;
import biz.metacode.clients.calcscript.interpreter.Invocable;
import biz.metacode.clients.calcscript.interpreter.SharedArray;
import biz.metacode.clients.calcscript.interpreter.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    },
    SORT_BY_MAPPING {
        public void invoke(final ExecutionContext context) throws InterruptedException {
            final Invocable mapping = context.pop();
            SharedArray array = (SharedArray) context.pop();
            try {
                List<Value> values = new ArrayList<Value>(array);
                Collections.sort(values, new Comparator<Value>() {
                    public int compare(Value first, Value second) {
                        int result = first.compareTo(second);
                        context.pushDouble(result);
                        try {
                            mapping.invoke(context);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        return (int) context.popDouble();
                    }
                });
                SharedArray sorted = context.acquireArray();
                sorted.addAll(values);
                context.pushArray(sorted);
            } finally {
                array.release();
            }
        }
    },
    CONCATENATE {
        public void invoke(final ExecutionContext context) throws InterruptedException {
            SharedArray first = (SharedArray) context.pop();
            SharedArray second = (SharedArray) context.pop();
            try {
                SharedArray result = context.acquireArray();
                result.addAll(second);
                result.addAll(first);
                context.pushArray(result);
            } finally {
                second.release();
                first.release();
            }
        }
    },
    SUBSTRACT {
        public void invoke(final ExecutionContext context) throws InterruptedException {
            SharedArray first = (SharedArray) context.pop();
            SharedArray second = (SharedArray) context.pop();
            try {
                SharedArray result = context.acquireArray();
                result.addAll(second);
                result.removeAll(first);
                context.pushArray(result);
            } finally {
                second.release();
                first.release();
            }
        }
    },
    REPEAT {
        public void invoke(final ExecutionContext context) throws InterruptedException {
            SharedArray first = (SharedArray) context.pop();
            double times = context.popDouble();
            try {
                SharedArray result = context.acquireArray();
                for (int i = (int) times - 1; i >= 0; i--) {
                    if (Thread.interrupted()) {
                        throw new InterruptedException();
                    }
                    result.addAll(first);
                }
                context.pushArray(result);
            } finally {
                first.release();
            }
        }
    }
}
