
package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.ExecutionContext;
import biz.metacode.clients.calcscript.interpreter.Invocable;
import biz.metacode.clients.calcscript.interpreter.SharedArray;
import biz.metacode.clients.calcscript.interpreter.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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
    },
    JOIN {
        public void invoke(final ExecutionContext context) throws InterruptedException {
            String separator = context.popString();
            SharedArray first = (SharedArray) context.pop();
            try {
                StringBuilder sb = new StringBuilder();
                Iterator<Value> iterator = first.iterator();
                while (iterator.hasNext()) {
                    sb.append(iterator.next());
                    if (iterator.hasNext()) {
                        sb.append(separator);
                    }
                }
                context.pushString(sb.toString());
            } finally {
                first.release();
            }
        }
    },
    JOIN_ARRAYS {
        public void invoke(final ExecutionContext context) throws InterruptedException {
            SharedArray first = (SharedArray) context.pop();
            SharedArray second = (SharedArray) context.pop();
            try {
                SharedArray result = context.acquireArray();
                Iterator<Value> iterator = second.iterator();
                while (iterator.hasNext()) {
                    result.add(iterator.next());
                    if (iterator.hasNext()) {
                        result.addAll(first);
                    }
                }
                context.pushArray(result);
            } finally {
                second.release();
                first.release();
            }
        }
    },
    FOLD {
        public void invoke(final ExecutionContext context) throws InterruptedException {
            Invocable block = context.pop();
            SharedArray array = (SharedArray) context.pop();
            try {
                Value accumulator = null;
                Iterator<Value> iterator = array.iterator();
                while (iterator.hasNext()) {
                    if (accumulator == null) {
                        accumulator = iterator.next();
                    } else {
                        context.push(iterator.next());
                        context.push(accumulator);
                        block.invoke(context);
                        accumulator.release();
                        if (Thread.interrupted()) {
                            throw new InterruptedException();
                        }
                        accumulator = context.pop();
                    }
                }
                context.push(accumulator);
                accumulator.release();
            } finally {
                array.release();
            }
        }
    },
    SPLIT_AROUND_MATCHES {
        public void invoke(final ExecutionContext context) throws InterruptedException {
            SharedArray matches = (SharedArray) context.pop();
            SharedArray array = (SharedArray) context.pop();
            try {
                SharedArray result = context.acquireArray();
                SharedArray matchResult = context.acquireArray();
                for (int i = 0, l = array.size(); i < l; i++) {
                    if (isMatch(array, i, matches)) {
                        result.add(context.convertToValue(matchResult));
                        matchResult = context.acquireArray();
                        i += matches.size() - 1;
                    } else {
                        matchResult.add(array.get(i));
                    }
                }
                result.add(context.convertToValue(matchResult));
                context.pushArray(result);
            } finally {
                matches.release();
                array.release();
            }
        }

        private boolean isMatch(SharedArray first, int position, SharedArray find) {
            for (int i = 0, l = find.size(); i < l; i++) {
                if (!first.get(i + position).equals(find.get(i))) {
                    return false;
                }
            }
            return true;
        }
    },
    SPLIT_INTO_GROUPS {
        public void invoke(final ExecutionContext context) throws InterruptedException {
            SharedArray array = (SharedArray) context.pop();
            int groupSize = (int) context.popDouble();
            try {
                SharedArray result = context.acquireArray();
                SharedArray groupResult = context.acquireArray();
                for (int i = 0, l = array.size(); i < l; i++) {
                    for (int j = 0; j < groupSize && i + j < l; j++) {
                        groupResult.add(array.get(i + j));
                    }
                    result.add(context.convertToValue(groupResult));
                    groupResult = context.acquireArray();
                    i += groupSize - 1;
                }
                context.pushArray(result);
            } finally {
                array.release();
            }
        }
    },
    EACH {
        public void invoke(ExecutionContext context) throws InterruptedException {
            Invocable function = context.pop();
            SharedArray array = (SharedArray) context.pop();
            try {
                for (Value value : array) {
                    context.push(value);
                    function.invoke(context);
                }
            } finally {
                array.release();
            }
        }
    },
    EVERY_NTH_ELEMENT {

        public void invoke(ExecutionContext context) throws InterruptedException {

            SharedArray array = (SharedArray) context.pop();
            double step = context.popDouble();
            try {
                if (step != 0) {
                    SharedArray result = context.acquireArray();
                    int s = (int) Math.abs(step);
                    for (int i = 0, l = array.size(); i < l; i += s) {
                        result.add(array.get(i));
                    }
                    if (step < 0) {
                        Collections.reverse(result);
                    }
                    context.pushArray(result);
                }
            } finally {
                array.release();
            }
        }

    },
    UNION {
        public void invoke(ExecutionContext context) throws InterruptedException {
            SharedArray first = (SharedArray) context.pop();
            SharedArray second = (SharedArray) context.pop();
            try {
                SharedArray result = context.acquireArray();
                result.addAll(union(first, second));
                context.pushArray(result);
            } finally {
                second.release();
                first.release();
            }
        }
    },
    INTERSECTION {
        public void invoke(ExecutionContext context) throws InterruptedException {
            SharedArray first = (SharedArray) context.pop();
            SharedArray second = (SharedArray) context.pop();
            try {
                SharedArray result = context.acquireArray();
                result.addAll(intersection(first, second));
                context.pushArray(result);
            } finally {
                second.release();
                first.release();
            }
        }
    },
    SYMMETRIC_DIFFERENCE {
        public void invoke(ExecutionContext context) throws InterruptedException {
            SharedArray first = (SharedArray) context.pop();
            SharedArray second = (SharedArray) context.pop();
            try {
                SharedArray result = context.acquireArray();
                List<Value> temporary = union(first, second);
                temporary.removeAll(intersection(first, second));
                result.addAll(temporary);
                context.pushArray(result);
            } finally {
                second.release();
                first.release();
            }
        }
    };

    protected <T> List<T> union(List<T> list1, List<T> list2) {
        Set<T> set = new TreeSet<T>();

        set.addAll(list1);
        set.addAll(list2);

        return new ArrayList<T>(set);
    }

    protected <T> List<T> intersection(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<T>();

        for (T t : list1) {
            if (list2.contains(t)) {
                list.add(t);
            }
        }

        return list;
    }
}
