
package biz.metacode.calcscript.interpreter.builtins;

import biz.metacode.calcscript.interpreter.ExecutionContext;
import biz.metacode.calcscript.interpreter.Invocable;
import biz.metacode.calcscript.interpreter.SelfDescribing;
import biz.metacode.calcscript.interpreter.SharedArray;
import biz.metacode.calcscript.interpreter.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Operators that work on arrays.
 */
public enum ArrayOperators implements Invocable, SelfDescribing {
    /**
     * Executes a block over all elements of the array. Takes two arguments
     * (block and array) leaves one (array).
     */
    MAP {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) throws InterruptedException {
            Value executable = context.pop();
            SharedArray list = context.pop().asArray();
            context.markPosition();
            try {
                for (Value object : list) {
                    context.push(object);
                    executable.invoke(context);
                }
            } finally {
                list.release();
                executable.release();
            }
            context.pushArray(context.extractMarkedArray());
        }

        /**
         * {@inheritDoc}
         */
        public String getExampleUsage() {
            return "[1 2 3]{3*}<name>";
        }
    },
    /**
     * Returns length of the array. Takes one argument (array), leaves one
     * argument (number).
     */
    GET_LENGTH {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) {
            SharedArray array = context.pop().asArray();
            context.pushDouble(array.size());
            array.release();
        }

        /**
         * {@inheritDoc}
         */
        public String getExampleUsage() {
            return "[1 2 3]<name>";
        }
    },
    /**
     * Extracts elements of an array onto the stack. Takes one argument (array),
     * leaves {@code array.length} elements on stack.
     */
    EXTRACT {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) {
            SharedArray array = context.pop().asArray();
            try {
                for (Value value : array) {
                    context.push(value);
                }
            } finally {
                array.release();
            }
        }

        /**
         * {@inheritDoc}
         */
        public String getExampleUsage() {
            return "[1 2 3]<name>";
        }
    },
    /**
     * Sorts elements in an array using {@link Value#compareTo(Value)} method.
     * Takes one array and leaves one array on stack.
     */
    SORT {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) throws InterruptedException {
            SharedArray array = context.pop().asArray();
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

        /**
         * {@inheritDoc}
         */
        public String getExampleUsage() {
            return "[3 1 4 2]<name>";
        }
    },
    /**
     * Sorts elements in an array using block as a comparator. Takes a block and
     * an array and leaves one array on stack.
     */
    SORT_BY_MAPPING {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) throws InterruptedException {
            final Invocable mapping = context.pop();
            final SharedArray array = context.pop().asArray();
            try {
                final List<Value> values = new ArrayList<Value>(array);
                Collections.sort(values, new Comparator<Value>() {
                    public int compare(final Value first, final Value second) {
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
                // comparator could set interruption flag
                Operators.interruptionPoint();
                SharedArray sorted = context.acquireArray();
                sorted.addAll(values);
                context.pushArray(sorted);
            } finally {
                array.release();
            }
        }

        /**
         * {@inheritDoc}
         */
        public String getExampleUsage() {
            return "[3 1 4 2]{-1*}<name>";
        }
    },
    /**
     * Joins two arrays together. Takes two arrays and leaves one on the stack.
     */
    CONCATENATE {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) throws InterruptedException {
            SharedArray first = context.pop().asArray();
            SharedArray second = context.pop().asArray();
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

        /**
         * {@inheritDoc}
         */
        public String getExampleUsage() {
            return "[1 2][3 4]<name>";
        }
    },
    /**
     * Removes elements from one array that are present in second one. Takes two
     * arrays and leaves one.
     */
    SUBSTRACT {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) throws InterruptedException {
            SharedArray first = context.pop().asArray();
            SharedArray second = context.pop().asArray();
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

        /**
         * {@inheritDoc}
         */
        public String getExampleUsage() {
            return "[1 2][3 1]<name>";
        }
    },
    /**
     * Repeats array given number of times. Takes an array and a number, leaves
     * repeated array on stack.
     */
    REPEAT {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) throws InterruptedException {
            SharedArray first = context.pop().asArray();
            double times = context.popDouble();
            try {
                SharedArray result = context.acquireArray();
                for (int i = (int) times - 1; i >= 0; i--) {
                    Operators.interruptionPoint();
                    result.addAll(first);
                }
                context.pushArray(result);
            } finally {
                first.release();
            }
        }

        /**
         * {@inheritDoc}
         */
        public String getExampleUsage() {
            return "[1 2 3]2<name>";
        }
    },
    /**
     * Joins an array using string separator. Takes one array and string
     * separator, leaves joined string on stack.
     */
    JOIN_BY_SEPARATOR {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) throws InterruptedException {
            String separator = context.popString();
            SharedArray first = context.pop().asArray();
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

        /**
         * {@inheritDoc}
         */
        public String getExampleUsage() {
            return "[1 2 3]','<name>";
        }
    },
    /**
     * Join two arrays together. Takes two arrays and leaves one joined array on
     * stack.
     */
    JOIN_ARRAYS {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) throws InterruptedException {
            SharedArray first = context.pop().asArray();
            SharedArray second = context.pop().asArray();
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

        /**
         * {@inheritDoc}
         */
        public String getExampleUsage() {
            return "[1 2 3][0]<name>";
        }
    },
    /**
     * Reduce an array using block to single value. Takes a block and an array
     * and leaves single value (type depends on the block).
     */
    FOLD {
        /**
         * {@inheritDoc}
         */
        @SuppressWarnings("checkstyle:CyclomaticComplexity")
        public void invoke(final ExecutionContext context) throws InterruptedException {
            Invocable block = context.pop();
            SharedArray array = context.pop().asArray();
            try {
                boolean shouldCloseAccumulator = false;
                Value accumulator = null;
                for (Value value : array) {
                    if (accumulator == null) {
                        accumulator = value;
                    } else {
                        context.push(accumulator);
                        context.push(value);
                        block.invoke(context);
                        if (shouldCloseAccumulator) {
                            accumulator.release();
                        }
                        Operators.interruptionPoint();
                        accumulator = context.pop();
                        shouldCloseAccumulator = true;
                    }
                }
                if (accumulator != null) {
                    context.push(accumulator);
                }
                if (shouldCloseAccumulator && accumulator != null) {
                    accumulator.release();
                }
            } finally {
                array.release();
            }
        }

        /**
         * {@inheritDoc}
         */
        public String getExampleUsage() {
            return "[1 2 3]{+}<name>";
        }
    },
    /**
     * Split an array around matches that are determined by the second array.
     * Takes two arrays as arguments and leaves one nested array on stack.
     */
    SPLIT_AROUND_MATCHES {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) throws InterruptedException {
            SharedArray matches = context.pop().asArray();
            SharedArray array = context.pop().asArray();
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

        private boolean isMatch(final SharedArray first, final int position,
                final SharedArray find) {
            for (int i = 0, l = find.size(); i < l; i++) {
                if (!first.get(i + position).equals(find.get(i))) {
                    return false;
                }
            }
            return true;
        }

        /**
         * {@inheritDoc}
         */
        public String getExampleUsage() {
            return "[1 0 2 0 2][0]<name>";
        }
    },
    /**
     * Splits an array into groups of specified size. Takes an array and group
     * size (number) as arguments, leaves one nested array on stack.
     */
    SPLIT_INTO_GROUPS {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) throws InterruptedException {
            SharedArray array = context.pop().asArray();
            int groupSize = (int) context.popDouble();
            try {
                SharedArray result = context.acquireArray();
                SharedArray groupResult = null;
                for (int i = 0, l = array.size(); i < l; i++) {
                    for (int j = 0; j < groupSize && i + j < l; j++) {
                        if (groupResult == null) {
                            groupResult = context.acquireArray();
                        }
                        groupResult.add(array.get(i + j));
                    }
                    result.add(context.convertToValue(groupResult));
                    groupResult = null;
                    i += groupSize - 1;
                }
                context.pushArray(result);
            } finally {
                array.release();
            }
        }

        /**
         * {@inheritDoc}
         */
        public String getExampleUsage() {
            return "[1 2 3 4]2<name>";
        }
    },
    /**
     * Executes block over all elements in an array. Takes block and array,
     * doesn't leave any explicit values but executing the block may leave some.
     */
    EACH {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) throws InterruptedException {
            Invocable function = context.pop();
            SharedArray array = context.pop().asArray();
            try {
                for (Value value : array) {
                    context.push(value);
                    function.invoke(context);
                }
            } finally {
                array.release();
            }
        }

        /**
         * {@inheritDoc}
         */
        public String getExampleUsage() {
            return "[1 2 3]{2*}<name>";
        }
    },
    /**
     * Takes each n-th element from an array. Takes array and step (number) and
     * leaves an array on stack.
     */
    EVERY_NTH_ELEMENT {

        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) throws InterruptedException {

            SharedArray array = context.pop().asArray();
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

        /**
         * {@inheritDoc}
         */
        public String getExampleUsage() {
            return "[1 2 3]2<name>";
        }

    },
    /**
     * Set union - treats two arrays as sets and computes a union. Takes two
     * arrays and leaves one on stack.
     */
    UNION {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) throws InterruptedException {
            SharedArray first = context.pop().asArray();
            SharedArray second = context.pop().asArray();
            try {
                SharedArray result = context.acquireArray();
                result.addAll(union(first, second));
                context.pushArray(result);
            } finally {
                second.release();
                first.release();
            }
        }

        /**
         * {@inheritDoc}
         */
        public String getExampleUsage() {
            return "[1 2 3][2 4 5]<name>";
        }
    },
    /**
     * Set intersection - treats two arrays as sets and computes an
     * intersection. Takes two arrays and leaves one on stack.
     */
    INTERSECTION {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) throws InterruptedException {
            SharedArray first = context.pop().asArray();
            SharedArray second = context.pop().asArray();
            try {
                SharedArray result = context.acquireArray();
                result.addAll(intersection(first, second));
                context.pushArray(result);
            } finally {
                second.release();
                first.release();
            }
        }

        /**
         * {@inheritDoc}
         */
        public String getExampleUsage() {
            return "[1 2 3][2 4 5]<name>";
        }
    },
    /**
     * Set symmetric difference - treats two arrays as sets and computes a
     * symmetric difference. Takes two arrays and leaves one on stack.
     */
    SYMMETRIC_DIFFERENCE {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) throws InterruptedException {
            SharedArray first = context.pop().asArray();
            SharedArray second = context.pop().asArray();
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

        /**
         * {@inheritDoc}
         */
        public String getExampleUsage() {
            return "[1 2 3][2 4 5]<name>";
        }
    },
    /**
     * Takes all elements from the array that have index less than given
     * argument. Takes an array and number and leaves one array on stack.
     */
    INDEX_LESS_THAN {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) throws InterruptedException {
            SharedArray first = context.pop().asArray();
            int index = (int) context.popDouble();
            try {
                SharedArray result = context.acquireArray();
                for (int i = 0; i < index; i++) {
                    result.add(first.get(i));
                }
                context.pushArray(result);
            } finally {
                first.release();
            }
        }

        /**
         * {@inheritDoc}
         */
        public String getExampleUsage() {
            return "[1 2 3]1<name>";
        }
    },
    /**
     * Takes all elements from the array that have index greater than given
     * argument. Takes an array and number and leaves one array on stack.
     */
    INDEX_GREATER_THAN {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) throws InterruptedException {
            SharedArray first = context.pop().asArray();
            int index = (int) context.popDouble();
            try {
                SharedArray result = context.acquireArray();
                for (int i = index; i < first.size(); i++) {
                    result.add(first.get(i));
                }
                context.pushArray(result);
            } finally {
                first.release();
            }
        }

        /**
         * {@inheritDoc}
         */
        public String getExampleUsage() {
            return "[1 2 3]1<name>";
        }
    },
    /**
     * Takes one element that have index equal to given, leaves nothing on stack
     * if index is invalid. Takes two arguments - an array and a number and
     * leaves one value or none on stack.
     */
    GET_ELEMENT {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) throws InterruptedException {
            SharedArray first = context.pop().asArray();
            int index = (int) context.popDouble();
            try {
                if (index < first.size()) {
                    context.push(first.get(index));
                }
            } finally {
                first.release();
            }
        }

        /**
         * {@inheritDoc}
         */
        public String getExampleUsage() {
            return "[1 2 3]1<name>";
        }
    },
    /**
     * Creates an array from {@code 0} to given value (excluding it). Takes one
     * number and leaves one array on stack.
     */
    CREATE_ARRAY {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) throws InterruptedException {
            int upTo = (int) context.popDouble();
            SharedArray result = context.acquireArray();
            for (int i = 0; i < upTo; i++) {
                result.add(context.convertToValue(i));
            }
            context.pushArray(result);
        }

        /**
         * {@inheritDoc}
         */
        public String getExampleUsage() {
            return "5<name>";
        }
    },
    /**
     * Returns the index of value in array or {@code -1} if it is not found.
     * Takes one array and one value as arguments and leaves one number on
     * stack.
     */
    INDEX_OF {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) throws InterruptedException {
            SharedArray first = context.pop().asArray();
            Value second = context.pop();
            try {
                context.pushDouble(first.indexOf(second));
            } finally {
                second.release();
                first.release();
            }
        }

        /**
         * {@inheritDoc}
         */
        public String getExampleUsage() {
            return "[1 2 3]2<name>";
        }
    },
    /**
     * Extracts first element from the array. Takes one array and leaves a value
     * and an array on stack.
     */
    UNCONS {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) throws InterruptedException {
            Value array = context.pop();
            try {
                SharedArray result = array.duplicate().asArray();
                Value first = result.get(0);
                context.pushArray(result);
                context.push(first);
                result.remove(0);
            } finally {
                array.release();
            }
        }

        /**
         * {@inheritDoc}
         */
        public String getExampleUsage() {
            return "[1 2 3]<name>";
        }
    },
    /**
     * Extracts last element from the array. Takes one array and leaves a value
     * and an array on stack.
     */
    UNCONS_RIGHT {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) throws InterruptedException {
            Value array = context.pop();
            try {
                SharedArray result = array.duplicate().asArray();
                Value last = result.get(result.size() - 1);
                context.pushArray(result);
                context.push(last);
                result.remove(result.size() - 1);
            } finally {
                array.release();
            }
        }

        /**
         * {@inheritDoc}
         */
        public String getExampleUsage() {
            return "[1 2 3]<name>";
        }
    },
    /**
     * Transposes array rows with columns. Takes one array as argument and
     * leaves one array on stack.
     */
    ZIP {
        /**
         * {@inheritDoc}
         */
        public void invoke(final ExecutionContext context) throws InterruptedException {
            SharedArray array = context.pop().asArray();
            try {
                int maxSize = findLargestSize(array);
                SharedArray result = context.acquireArray();
                for (int i = 0; i < maxSize; i++) {
                    SharedArray part = context.acquireArray();
                    for (int j = 0; j < array.size(); j++) {
                        SharedArray element = array.get(j).asArray();
                        if (i < element.size()) {
                            part.add(element.get(i));
                        }
                    }
                    result.add(context.convertToValue(part));
                }
                context.pushArray(result);
            } finally {
                array.release();
            }
        }

        /**
         * {@inheritDoc}
         */
        public String getExampleUsage() {
            return "[[1 2][3 4][5 6]]<name>";
        }

        private int findLargestSize(final SharedArray array) {
            // find largest array
            int maxSize = 0;
            for (int i = 0; i < array.size(); i++) {
                int size = array.get(i).asArray().size();
                if (size > maxSize) {
                    maxSize = size;
                }
            }
            return maxSize;
        }
    };

    /**
     * Treats parameters as sets and creates a set union returning unique
     * elements from both lists.
     *
     * @param list1 First list.
     * @param list2 Second list.
     * @param <T> Parameter type.
     * @return List of elements from both parameters without duplicates.
     */
    protected <T> List<T> union(final List<T> list1, final List<T> list2) {
        Set<T> set = new TreeSet<T>();

        set.addAll(list1);
        set.addAll(list2);

        return new ArrayList<T>(set);
    }

    /**
     * Treats parameters as sets and creates a set intersection returning only
     * elements that exist in both lists.
     *
     * @param list1 First list.
     * @param list2 Second list.
     * @param <T> Parameter type.
     * @return List of elements that are present in both lists.
     */
    protected <T> List<T> intersection(final List<T> list1, final List<T> list2) {
        List<T> list = new ArrayList<T>();

        for (T t : list1) {
            if (list2.contains(t)) {
                list.add(t);
            }
        }

        return list;
    }
}
