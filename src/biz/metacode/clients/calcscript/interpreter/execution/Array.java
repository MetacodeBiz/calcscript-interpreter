
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

    @Override
    public Iterator<Value> iterator() {
        return new ArrayIterator(this, this.entries.iterator());
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

    private static class ArrayIterator implements Iterator<Value> {

        private final Iterator<? extends Value> iterator;

        private final SharedArray parent;

        public ArrayIterator(SharedArray parent, Iterator<? extends Value> iterator) {
            this.iterator = iterator;
            this.parent = parent;
        }

        @Override
        public boolean hasNext() {
            boolean next = iterator.hasNext();
            if (!next) {
                parent.release();
            }
            return next;
        }

        @Override
        public Value next() {
            return iterator.next();
        }

        @Override
        public void remove() {
            iterator.remove();
        }

    }

    @Override
    public int size() {
        return this.entries.size();
    }

    @Override
    public boolean isEmpty() {
        return this.entries.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.entries.contains(o);
    }

    @Override
    public Object[] toArray() {
        return this.entries.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.entries.toArray(a);
    }

    @Override
    public boolean remove(Object o) {
        return this.entries.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.entries.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Value> c) {
        return this.entries.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return this.entries.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.entries.retainAll(c);
    }

    @Override
    public boolean add(Value e) {
        return this.entries.add(e);
    }

    @Override
    public void release() {
        this.relinquish();
    }

    @Override
    public Value duplicate() {
        Array duplicate = this.pool.acquire();
        duplicate.addAll(this);
        return duplicate;
    }
}
