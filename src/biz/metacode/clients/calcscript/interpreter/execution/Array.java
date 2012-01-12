
package biz.metacode.clients.calcscript.interpreter.execution;

import biz.metacode.clients.calcscript.interpreter.SharedArray;
import biz.metacode.clients.calcscript.interpreter.Value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Array extends Value implements SharedArray {

    private static final long serialVersionUID = -7480425864645673589L;

    private final ArrayList<Value> entries = new ArrayList<Value>();

    private transient final Pool<Array> pool;

    Array(Pool<Array> pool) {
        this.pool = pool;
    }

    public List<Value> consume() {
        List<Value> clone = new ArrayList<Value>(entries);
        this.relinquish();
        return clone;
    }

    public void relinquish() {
        if (this.pool != null) {
            this.pool.relinquish(this);
        }
    }

    public void clear() {
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

    public void release() {
        this.relinquish();
    }

    @Override
    public Value duplicate() {
        Array duplicate = this.pool.acquire();
        for (Value value : this.entries) {
            duplicate.add(value.duplicate());
        }
        return duplicate;
    }
}
