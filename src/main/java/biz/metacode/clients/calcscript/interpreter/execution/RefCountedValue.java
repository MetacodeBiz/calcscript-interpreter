
package biz.metacode.clients.calcscript.interpreter.execution;

import biz.metacode.clients.calcscript.interpreter.Value;

public abstract class RefCountedValue extends Value {

    private static final long serialVersionUID = 6994581318461354474L;

    private int refCounter;

    public void acquire() {
        refCounter++;
    }

    public void release() {
        --refCounter;
        assert refCounter >= 0 : "Object " + this + " released more than once!";
        if (refCounter == 0) {
            relinquish();
        }
    }

    protected abstract void relinquish();

    protected boolean isShared() {
        return refCounter > 0;
    }

}
