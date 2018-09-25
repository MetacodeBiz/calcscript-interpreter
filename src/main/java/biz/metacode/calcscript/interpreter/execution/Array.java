
package biz.metacode.calcscript.interpreter.execution;

import biz.metacode.calcscript.interpreter.SharedArray;
import biz.metacode.calcscript.interpreter.Value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

class Array extends RefCountedValue implements SharedArray, PooledObject {

    private static final long serialVersionUID = -7480425864645673589L;

    private final ArrayList<Value> entries = new ArrayList<Value>();

    private transient Pool<Array> pool;

    Array(final Pool<Array> pool) {
        this.pool = pool;
    }

    @Override
    public SharedArray asArray() {
        return this;
    }

    @Override
    protected void relinquish() {
        this.pool.destroy(this);
    }

    public void clear() {
        checkModification();
        for (Value value : entries) {
            value.release();
        }
        this.entries.clear();
    }

    public Iterator<Value> iterator() {
        return this.entries.iterator();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Iterator<? extends Value> iterator = this.entries.iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next().toSource());
            if (iterator.hasNext()) {
                sb.append(' ');
            }
        }
        return sb.append("]").toString();
    }

    @Override
    public int getPriority() {
        return 2;
    }

    public int size() {
        return this.entries.size();
    }

    public boolean isEmpty() {
        return this.entries.isEmpty();
    }

    public boolean contains(final Object object) {
        return this.entries.contains(object);
    }

    public Object[] toArray() {
        return this.entries.toArray();
    }

    public <T> T[] toArray(final T[] array) {
        return this.entries.toArray(array);
    }

    public boolean remove(final Object object) {
        checkModification();
        int index = this.indexOf(object);
        if (index >= 0) {
            Value originalValue = this.get(index);
            this.entries.remove(object);
            originalValue.release();
            return true;
        }
        return false;
    }

    public boolean containsAll(final Collection<?> collection) {
        return this.entries.containsAll(collection);
    }

    public boolean addAll(final Collection<? extends Value> collection) {
        checkModification();
        for (Value value : collection) {
            if (value instanceof RefCountedValue) {
                ((RefCountedValue) value).acquire();
            }
        }
        return this.entries.addAll(collection);
    }

    public boolean removeAll(final Collection<?> collection) {
        checkModification();
        for (Object value : collection) {
            // remove all occurences of object
            while (remove(value)) {
                // do nothing
            }
        }
        return false;
    }

    public boolean retainAll(final Collection<?> collection) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean add(final Value value) {
        checkModification();
        if (value instanceof RefCountedValue) {
            ((RefCountedValue) value).acquire();
        }
        return this.entries.add(value);
    }

    @Override
    public Value duplicate() {
        Array duplicate = this.pool.create();
        for (Value value : this.entries) {
            duplicate.add(value.duplicate());
        }
        return duplicate;
    }

    public void attachToPool(final PoolProvider poolProvider) {
        this.pool = poolProvider.getPool(Array.class);
        for (Value value : entries) {
            if (value instanceof PooledObject) {
                ((PooledObject) value).attachToPool(poolProvider);
            }
        }
    }

    @Override
    public Type getType() {
        return Type.ARRAY;
    }

    @Override
    public boolean toBoolean() {
        return !this.isEmpty();
    }

    public boolean addAll(final int index, final Collection<? extends Value> collection) {
        return this.entries.addAll(collection);
    }

    public Value get(final int index) {
        return this.entries.get(index);
    }

    public Value set(final int index, final Value element) {
        if (element instanceof RefCountedValue) {
            ((RefCountedValue) element).acquire();
        }
        Value previous = this.entries.set(index, element);
        if (previous != null) {
            previous.release();
        }
        return previous;
    }

    public void add(final int index, final Value element) {
        if (element instanceof RefCountedValue) {
            ((RefCountedValue) element).acquire();
        }
        this.entries.add(index, element);
    }

    public Value remove(final int index) {
        Value value = this.entries.remove(index);
        value.release();
        return value;
    }

    public int indexOf(final Object object) {
        return this.entries.indexOf(object);
    }

    public int lastIndexOf(final Object object) {
        return this.entries.lastIndexOf(object);
    }

    public ListIterator<Value> listIterator() {
        return this.entries.listIterator();
    }

    public ListIterator<Value> listIterator(final int index) {
        return this.entries.listIterator(index);
    }

    public List<Value> subList(final int fromIndex, final int toIndex) {
        return this.entries.subList(fromIndex, toIndex);
    }

    private void checkModification() {
        if (isShared()) {
            throw new IllegalStateException("Cannot modify shared object.");
        }
    }
}
