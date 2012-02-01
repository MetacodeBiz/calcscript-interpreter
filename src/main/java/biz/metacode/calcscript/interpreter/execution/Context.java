
package biz.metacode.calcscript.interpreter.execution;

import biz.metacode.calcscript.interpreter.ExecutionContext;
import biz.metacode.calcscript.interpreter.InvalidTypeException;
import biz.metacode.calcscript.interpreter.Invocable;
import biz.metacode.calcscript.interpreter.OverloadMissingException;
import biz.metacode.calcscript.interpreter.Value;
import biz.metacode.calcscript.interpreter.Value.Type;
import biz.metacode.calcscript.interpreter.ValueMissingException;
import biz.metacode.calcscript.interpreter.Value.Pair;
import biz.metacode.calcscript.interpreter.source.Program;

import java.util.Collection;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

class Context implements ExecutionContext, PoolProvider {

    private final NumericPool valuePool = new NumericPool();

    private final ArrayPool arrayPool = new ArrayPool();

    private final TextPool textPool = new TextPool();

    private Stack stack;

    private Memory memory;

    public Context() {
        this.clearStack();
    }

    public void setMemory(Memory memory) {
        this.memory = memory;
    }

    public void clearStack() {
        stack = new Stack(arrayPool);
    }

    public Array getData() {
        return stack.getData();
    }

    public void write(String name, Invocable element) {
        if (element instanceof RefCountedValue) {
            ((RefCountedValue) element).acquire();
        }
        Invocable previous = this.memory.write(name, element);
        if (previous instanceof Value) {
            ((Value) previous).release();
        }
    }

    public Invocable read(String name) {
        return this.memory.read(name);
    }

    public void push(Value element) {
        if (element instanceof RefCountedValue) {
            ((RefCountedValue) element).acquire();
        }
        this.stack.push(element);
    }

    public void pushDouble(double element) {
        this.push(convertToValue(element));
    }

    public void pushString(String element) {
        this.push(convertToValue(element));
    }

    public void pushBoolean(boolean value) {
        this.pushDouble(value ? 1 : 0);
    }

    public Value pop() {
        try {
            return stack.pop();
        } catch (EmptyStackException e) {
            throw new ValueMissingException(e);
        }
    }

    public double popDouble() {
        Value result = pop();
        try {
            if (result instanceof Numeric) {
                return ((Numeric) result).get();
            } else {
                throw new InvalidTypeException(Type.NUMBER, result.getType());
            }
        } finally {
            result.release();
        }
    }

    public String popString() {
        Value result = pop();
        try {
            if (result instanceof Text) {
                return ((Text) result).get();
            } else {
                throw new InvalidTypeException(Type.STRING, result.getType());
            }
        } finally {
            result.release();
        }
    }

    public boolean popBoolean() {
        Value result = pop();
        try {
            return result.toBoolean();
        } finally {
            result.release();
        }
    }

    public Value peek() {
        try {
            return stack.peek();
        } catch (EmptyStackException e) {
            throw new ValueMissingException(e);
        }
    }

    public Value popAt(int index) {
        try {
            return stack.popAt(index);
        } catch (EmptyStackException e) {
            throw new ValueMissingException(e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ValueMissingException(e);
        }
    }

    public Value peekAt(int index) {
        try {
            return stack.peekAt(index);
        } catch (EmptyStackException e) {
            throw new ValueMissingException(e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ValueMissingException(e);
        }
    }

    public void markPosition() {
        stack.markPosition();
    }

    public Array extractMarkedArray() {
        return stack.extractMarkedArray();
    }

    public Array acquireArray() {
        return arrayPool.create();
    }

    public Value convertToValue(Collection<? extends Value> array) {
        if (array instanceof Array) {
            return (Array) array;
        } else {
            Array temporary = this.acquireArray();
            temporary.addAll(array);
            return temporary;
        }
    }

    public Value convertToValue(String string) {
        return textPool.create(string);
    }

    public Value convertToValue(double element) {
        return valuePool.create(element);
    }

    public void pushArray(Collection<? extends Value> array) {
        this.push(convertToValue(array));
    }

    @SuppressWarnings("unchecked")
    public <T extends PooledObject> Pool<T> getPool(Class<T> pooledObject) {
        if (Numeric.class.equals(pooledObject)) {
            return (Pool<T>) valuePool;
        }
        if (Text.class.equals(pooledObject)) {
            return (Pool<T>) textPool;
        }
        if (Array.class.equals(pooledObject)) {
            return (Pool<T>) arrayPool;
        }
        throw new IllegalArgumentException("This type of pool is not supported: " + pooledObject);
    }

    public Set<String> getRegisteredVariableNames() {
        return memory.keys();
    }

    public Iterator<Map.Entry<String, Invocable>> getRegisteredVariables() {
        return memory.iterator();
    }

    public Pair coerce(Value first, Value second) {
        if (first.getPriority() == second.getPriority()) {
            return new Pair(first, second);
        }
        while (first.getPriority() < second.getPriority()) {
            first = convertUp(first);
        }
        while (second.getPriority() < first.getPriority()) {
            second = convertUp(second);
        }
        return new Pair(first, second);
    }

    private Value convertUp(Value value) {
        if (value instanceof Numeric) {
            Array array = this.acquireArray();
            array.add(value);
            return array;
        } else if (value instanceof Array) {
            StringBuilder sb = new StringBuilder();
            for (Value element : (Array) value) {
                sb.append(element).append(' ');
            }

            if (sb.length() > 0) {
                sb.delete(sb.length() - 1, sb.length());
            }

            return textPool.create(sb.toString());
        } else if (value instanceof Text) {
            return Program.createInvocable(value.toString());
        }
        throw new OverloadMissingException(value.getType());
    }

    public void interruptionPoint() throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
    }

    public void setTrait(String trait) {
        valuePool.setTrait(trait);
        arrayPool.setTrait(trait);
        textPool.setTrait(trait);
    }

    public void remove(String name) {
        Invocable previous = memory.remove(name);
        if (previous instanceof Value) {
            ((Value) previous).release();
        }
    }
}
