
package biz.metacode.calcscript.interpreter;

import javax.annotation.Nonnull;

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

    /**
     * Converts this value to number. Note that for strings it will not attempt
     * to interpret the string as a double.
     * 
     * @return Numeric representation of this value.
     */
    public double toDouble() {
        return toBoolean() ? 1 : 0;
    }

    /**
     * Converts this value to boolean. Only {@code 0}, {@code ""}, {@code []}
     * and {@literal are {@code false}, everything else is {@code true}.
     * 
     * @return Boolean representation of this value.
     */
    public abstract boolean toBoolean();

    public String toSource() {
        return this.toString();
    }

    /**
     * Releases this object into its pool.
     * 
     * @see ExecutionContext#pop()
     */
    public void release() {
    }

    /**
     * Return a type of this value.
     * 
     * @return Value type.
     */
    @Nonnull
    public abstract Type getType();

    /**
     * {@inheritDoc}
     */
    public int compareTo(Value o) {
        return this.toString().compareTo(o.toString());
    }

    /**
     * Returns this value as an array or throws {@link InvalidTypeException} if
     * the value is not an array. Note that this value and the array represent
     * the same object - if this value needs to be released then invoke only one
     * release method: {@link SharedArray#release()} or {@link #release()}.
     * 
     * @return This value as an array.
     */
    @Nonnull
    public SharedArray asArray() {
        throw new InvalidTypeException(Type.ARRAY, getType());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Value) {
            return this.getType() == ((Value)obj).getType()
                    && this.toString().equals(obj.toString());
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
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
    @Nonnull
    public Pair order(@Nonnull
    Value other) {
        if (other.getPriority() > this.getPriority()) {
            return new Pair(other, this);
        }
        return new Pair(this, other);
    }

    /**
     * Value type.
     */
    public enum Type {
        /** Numeric type */
        NUMBER,
        /** Collection of other values */
        ARRAY,
        /** Text - collection of character */
        STRING,
        /** A function */
        BLOCK,
        /** Unknown type */
        OTHER;

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

        /**
         * Creates a new ordered pair of values.
         * 
         * @param first First value.
         * @param second Second value.
         */
        public Pair(Value first, Value second) {
            this.first = first;
            this.second = second;
        }

        /**
         * Returns a type name of this pair.
         * 
         * @return Pair type name.
         */
        @Nonnull
        public String getTypeName() {
            return this.first.getType() + "_" + this.second.getType();
        }
    }
}
