package biz.metacode.clients.calcscript.interpreter.execution;

import biz.metacode.clients.calcscript.interpreter.Invocable;

public abstract class Value implements Invocable {

    private static final long serialVersionUID = -1274916686056050022L;

    @Override
    public void invoke(Context context) {
        context.push(this);
    }

    protected abstract int getPriority();

    public Pair order(Value other) {
        if (other.getPriority() > this.getPriority()) {
            return new Pair(other, this);
        }
        return new Pair(this, other);
    }

    public static class Pair {
        public final Value first;
        public final Value second;

        public Pair(Value first, Value second) {
            this.first = first;
            this.second = second;
        }
    }
}
