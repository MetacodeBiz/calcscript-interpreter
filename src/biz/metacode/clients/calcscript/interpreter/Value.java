
package biz.metacode.clients.calcscript.interpreter;

/**
 * Base class for objects that can be manipulated during program execution.
 * Values can be placed on stack so that functions can use them as arguments or
 * return values. There are four basic types: numeric, array, string and block.
 */
public abstract class Value implements Invocable {

    private static final long serialVersionUID = -1274916686056050022L;

    /**
     * Invoke this value. By default it places this object onto the stack.
     */
    @Override
    public void invoke(ExecutionContext context) {
        context.push(this);
    }

    /**
     * Retrieves relative value priority.
     *
     * @return Value priority. Lower integers indicate lower priority.
     */
    protected abstract int getPriority();

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
    }
}
