
package biz.metacode.clients.calcscript.interpreter.execution;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Array extends Value implements Iterable<Value> {

    private static final long serialVersionUID = -7480425864645673589L;

    private final ArrayList<Value> entries = new ArrayList<Value>();

    private transient final ArrayPool pool;

    Array(ArrayPool pool) {
        this.pool = pool;
    }

    public List<Value> consume() {
        @SuppressWarnings("unchecked")
        List<Value> clone = (ArrayList<Value>) entries.clone();
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

    public void add(Value entry) {
        this.entries.add(entry);
    }

    public int length() {
        return this.entries.size();
    }

    @Override
    public Iterator<Value> iterator() {
        return new ArrayIterator(this.entries.iterator());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Iterator<Value> iterator = this.entries.iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next());
            if (iterator.hasNext()) {
                sb.append(' ');
            }
        }
        return sb.append("]").toString();
    }

    private class ArrayIterator implements Iterator<Value> {

        private final Iterator<Value> iterator;

        public ArrayIterator(Iterator<Value> iterator) {
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
        public Value next() {
            return iterator.next();
        }

        @Override
        public void remove() {
            iterator.remove();
        }

    }
}
