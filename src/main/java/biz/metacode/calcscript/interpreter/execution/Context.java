
package biz.metacode.calcscript.interpreter.execution;

import biz.metacode.calcscript.interpreter.ExecutionContext;
import biz.metacode.calcscript.interpreter.InvalidTypeException;
import biz.metacode.calcscript.interpreter.Invocable;
import biz.metacode.calcscript.interpreter.Value;
import biz.metacode.calcscript.interpreter.Value.Pair;
import biz.metacode.calcscript.interpreter.Value.Type;
import biz.metacode.calcscript.interpreter.ValueMissingException;
import biz.metacode.calcscript.interpreter.source.Program;

import java.util.Collection;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

class Context implements ExecutionContext, PoolProvider {

    private final NumericPool valuePool = new NumericPool();

    private final ArrayPool arrayPool = new ArrayPool();

    private final TextPool textPool = new TextPool();

    private Stack stack;

    private Memory memory;

    Context() {
        this.clearStack();
    }

    public void setMemory(final Memory memory) {
        this.memory = memory;
    }

    public void clearStack() {
        stack = new Stack(arrayPool);
    }

    public Array getData() {
        return stack.getData();
    }

    public void write(final String name, final @Nullable Invocable element) {
        if (element instanceof RefCountedValue) {
            ((RefCountedValue) element).acquire();
        }
        Invocable previous = this.memory.write(name, element);
        if (previous instanceof Value) {
            ((Value) previous).release();
        }
    }

    public Invocable read(final String name) {
        return this.memory.read(name);
    }

    public void push(final Value element) {
        if (element instanceof RefCountedValue) {
            ((RefCountedValue) element).acquire();
        }
        this.stack.push(element);
    }

    public void pushDouble(final double element) {
        this.push(convertToValue(element));
    }

    public void pushString(final String element) {
        this.push(convertToValue(element));
    }

    public void pushBoolean(final boolean value) {
        if (value) {
            this.pushDouble(1);
        } else {
            this.pushDouble(0);
        }
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

    public Value popAt(final int index) {
        try {
            return stack.popAt(index);
        } catch (EmptyStackException e) {
            throw new ValueMissingException(e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ValueMissingException(e);
        }
    }

    public Value peekAt(final int index) {
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

    public Value convertToValue(final Collection<? extends Value> array) {
        if (array instanceof Array) {
            return (Array) array;
        } else {
            Array temporary = this.acquireArray();
            temporary.addAll(array);
            return temporary;
        }
    }

    public Value convertToValue(final String string) {
        return textPool.create(string);
    }

    public Value convertToValue(final double element) {
        return valuePool.create(element);
    }

    public void pushArray(final Collection<? extends Value> array) {
        this.push(convertToValue(array));
    }

    @SuppressWarnings("unchecked")
    public <T extends PooledObject> Pool<T> getPool(final Class<T> pooledObject) {
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

    public Pair coerce(final Value first, final Value second) {
        Value firstValue = first;
        Value secondValue = second;
        if (firstValue.getPriority() == secondValue.getPriority()) {
            return new Pair(firstValue, secondValue);
        }
        while (firstValue.getPriority() < secondValue.getPriority()) {
            firstValue = convertUp(firstValue);
        }
        while (secondValue.getPriority() < firstValue.getPriority()) {
            secondValue = convertUp(secondValue);
        }
        return new Pair(firstValue, secondValue);
    }

    private Value convertUp(final Value value) {
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
        throw new IllegalArgumentException("Could not convert up type: " + value.getType());
    }

    public void setTrait(final String trait) {
        valuePool.setTrait(trait);
        arrayPool.setTrait(trait);
        textPool.setTrait(trait);
    }

    public void remove(final String name) {
        Invocable previous = memory.remove(name);
        if (previous instanceof Value) {
            ((Value) previous).release();
        }
    }
}
