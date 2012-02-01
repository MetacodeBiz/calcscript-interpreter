
package biz.metacode.calcscript.interpreter;

/**
 * Base class for objects that can be manipulated during program execution.
 * Values can be placed on stack so that functions can use them as arguments or
 * return values. There are four basic types: numeric, array, string and block.
 */
public abstract class Value implements Invocable, Comparable<Value> {

    private static final long serialVersionUID = -1274916686056050022L;

    /**
     * Invoke this value. By default it places this object onto the stack.
     *
     * @throws InterruptedException
     */
    public void invoke(ExecutionContext context) throws InterruptedException {
        context.push(this);
    }

    /**
     * Retrieves relative value priority.
     *
     * @return Value priority. Lower integers indicate lower priority.
     */
    public abstract int getPriority();

    /**
     * Creates a deep copy of this object that is separate from the original. If
     * current object is immutable it can return this as a duplicate.
     *
     * @return Deep copy of this.
     */
    public abstract Value duplicate();

    public double toDouble() {
        return toBoolean() ? 1 : 0;
    }

    public abstract boolean toBoolean();

    public void release() {
    }

    public abstract Type getType();

    public int compareTo(Value o) {
        return this.toString().compareTo(o.toString());
    }

    public SharedArray asArray() {
        throw new InvalidTypeException(Type.ARRAY, getType());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Value) {
            return this.getType() == ((Value) obj).getType()
                    && this.toString().equals(obj.toString());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    /**
     * Order two values (this and other) according to their priorities.
     *
     * @param other Second value
     * @return Ordered pair where first has higher or equal priority than second
     */
    public Pair order(Value other) {
        if (other.getPriority() > this.getPriority()) {
            return new Pair(other, this);
        }
        return new Pair(this, other);
    }

    public enum Type {
        NUMBER, ARRAY, STRING, BLOCK, OTHER;

        public String toString() {
            return name().toLowerCase();
        };
    }

    /**
     * Ordered pair of values.
     */
    public static class Pair {
        public final Value first;

        public final Value second;

        public Pair(Value first, Value second) {
            this.first = first;
            this.second = second;
        }

        public String getTypeName() {
            return this.first.getType() + "_" + this.second.getType();
        }
    }
}
