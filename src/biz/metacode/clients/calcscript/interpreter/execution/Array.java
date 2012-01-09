
package biz.metacode.clients.calcscript.interpreter.execution;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Array implements Iterable<Serializable>, Serializable {

    private static final long serialVersionUID = -7480425864645673589L;

    private final ArrayList<Serializable> entries = new ArrayList<Serializable>();

    private transient final ArrayPool pool;

    public Array(ArrayPool pool) {
        this.pool = pool;
    }

    public List<Serializable> consume() {
        @SuppressWarnings("unchecked")
        List<Serializable> clone = (ArrayList<Serializable>) entries.clone();
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

    public void add(Serializable entry) {
        this.entries.add(entry);
    }

    public int length() {
        return this.entries.size();
    }

    @Override
    public Iterator<Serializable> iterator() {
        return new ArrayIterator(this.entries.iterator());
    }

    @Override
    public String toString() {
        return entries.toString();
    }

    private class ArrayIterator implements Iterator<Serializable> {

        private final Iterator<Serializable> iterator;

        public ArrayIterator(Iterator<Serializable> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            boolean next = iterator.hasNext();
            if (!next) {
                Array.this.relinquish();
            }
            return next;
        }

        @Override
        public Serializable next() {
            return iterator.next();
        }

        @Override
        public void remove() {
            iterator.remove();
        }

    }
}
