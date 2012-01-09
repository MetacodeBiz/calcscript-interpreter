package biz.metacode.clients.calcscript.interpreter.execution;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Array implements Iterable<Object> {

    private final ArrayList<Object> entries = new ArrayList<Object>();

    private final ArrayPool pool;

    public Array(ArrayPool pool) {
        this.pool = pool;
    }

    public List<Object> consume() {
        @SuppressWarnings("unchecked")
        List<Object> clone = (ArrayList<Object>) entries.clone();
        this.pool.relinquish(this);
        return clone;
    }

    public void clear() {
        this.entries.clear();
    }

    public void add(Object entry) {
        this.entries.add(entry);
    }

    @Override
    public Iterator<Object> iterator() {
        return new ArrayIterator(this.entries.iterator());
    }

    @Override
    public String toString() {
        return entries.toString();
    }

    private class ArrayIterator implements Iterator<Object> {

        private final Iterator<Object> iterator;

        public ArrayIterator(Iterator<Object> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            boolean next = iterator.hasNext();
            if (!next) {
                pool.relinquish(Array.this);
            }
            return next;
        }

        @Override
        public Object next() {
            return iterator.next();
        }

        @Override
        public void remove() {
            iterator.remove();
        }

    }
}
