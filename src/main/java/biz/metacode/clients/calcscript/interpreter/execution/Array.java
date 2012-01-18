
package biz.metacode.clients.calcscript.interpreter.execution;

import biz.metacode.clients.calcscript.interpreter.SharedArray;
import biz.metacode.clients.calcscript.interpreter.Value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class Array extends RefCountedValue implements SharedArray, PooledObject {

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
        for (Value value : entries) {
            if (value instanceof RefCountedValue) {
                ((RefCountedValue) value).release();
            }
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
    protected int getPriority() {
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
        return this.entries.remove(o);
    }

    public boolean containsAll(Collection<?> c) {
        return this.entries.containsAll(c);
    }

    public boolean addAll(Collection<? extends Value> c) {
        return this.entries.addAll(c);
    }

    public boolean removeAll(Collection<?> c) {
        return this.entries.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        return this.entries.retainAll(c);
    }

    public boolean add(Value e) {
        return this.entries.add(e);
    }

    @Override
    public Value duplicate() {
        Array duplicate = this.pool.create();
        for (Value value : this.entries) {
            Value valueDuplicate = value.duplicate();
            if (valueDuplicate instanceof RefCountedValue) {
                ((RefCountedValue) valueDuplicate).acquire();
            }
            duplicate.add(valueDuplicate);
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
}
