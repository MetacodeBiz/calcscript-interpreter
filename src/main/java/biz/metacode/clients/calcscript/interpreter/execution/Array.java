
package biz.metacode.clients.calcscript.interpreter.execution;

import biz.metacode.clients.calcscript.interpreter.SharedArray;
import biz.metacode.clients.calcscript.interpreter.Value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

class Array extends RefCountedValue implements SharedArray, PooledObject {

    private static final long serialVersionUID = -7480425864645673589L;

    private final ArrayList<Value> entries = new ArrayList<Value>();

    private transient Pool<Array> pool;

    Array(Pool<Array> pool) {
        this.pool = pool;
    }

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
            sb.append(iterator.next());
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

    public boolean contains(Object o) {
        return this.entries.contains(o);
    }

    public Object[] toArray() {
        return this.entries.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return this.entries.toArray(a);
    }

    public boolean remove(Object o) {
        checkModification();
        int index = this.indexOf(o);
        if (index >= 0) {
            Value originalValue = this.get(index);
            this.entries.remove(o);
            originalValue.release();
            return true;
        }
        return false;
    }

    public boolean containsAll(Collection<?> c) {
        return this.entries.containsAll(c);
    }

    public boolean addAll(Collection<? extends Value> c) {
        checkModification();
        for (Value value : c) {
            if (value instanceof RefCountedValue) {
                ((RefCountedValue) value).acquire();
            }
        }
        return this.entries.addAll(c);
    }

    public boolean removeAll(Collection<?> c) {
        checkModification();
        for (Object value : c) {
            // remove all occurences of object
            while (remove(value)) {
            }
            ;
        }
        return false;
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet.");
        // return this.entries.retainAll(c);
    }

    public boolean add(Value e) {
        checkModification();
        if (e instanceof RefCountedValue) {
            ((RefCountedValue) e).acquire();
        }
        return this.entries.add(e);
    }

    @Override
    public Value duplicate() {
        Array duplicate = this.pool.create();
        for (Value value : this.entries) {
            duplicate.add(value.duplicate());
        }
        return duplicate;
    }

    public void attachToPool(PoolProvider poolProvider) {
        this.pool = poolProvider.getPool(Array.class);
        for (Value value : entries) {
            if (value instanceof PooledObject) {
                ((PooledObject) value).attachToPool(poolProvider);
            }
        }
    }

    @Override
    public String getTypeName() {
        return "array";
    }

    @Override
    public boolean toBoolean() {
        return !this.isEmpty();
    }

    public boolean addAll(int index, Collection<? extends Value> c) {
        return this.entries.addAll(c);
    }

    public Value get(int index) {
        return this.entries.get(index);
    }

    public Value set(int index, Value element) {
        if (element instanceof RefCountedValue) {
            ((RefCountedValue) element).acquire();
        }
        Value previous = this.entries.set(index, element);
        if (previous != null) {
            previous.release();
        }
        return previous;
    }

    public void add(int index, Value element) {
        if (element instanceof RefCountedValue) {
            ((RefCountedValue) element).acquire();
        }
        this.entries.add(index, element);
    }

    public Value remove(int index) {
        Value value = this.entries.remove(index);
        value.release();
        return value;
    }

    public int indexOf(Object o) {
        return this.entries.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return this.entries.lastIndexOf(o);
    }

    public ListIterator<Value> listIterator() {
        return this.entries.listIterator();
    }

    public ListIterator<Value> listIterator(int index) {
        return this.entries.listIterator(index);
    }

    public List<Value> subList(int fromIndex, int toIndex) {
        return this.entries.subList(fromIndex, toIndex);
    }

    private void checkModification() {
        if (isShared()) {
            throw new IllegalStateException("Cannot modify shared object.");
        }
    }
}
