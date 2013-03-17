
package biz.metacode.calcscript.interpreter.execution;

import biz.metacode.calcscript.interpreter.Value;

abstract class RefCountedValue extends Value {

    transient String trait;

    private static final long serialVersionUID = 6994581318461354474L;

    private int refCounter;

    public void acquire() {
        refCounter++;
    }

    @Override
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

    /*
     * Add slash at the end to enable reference tracking * protected void
     * finalize() throws Throwable {
     * System.out.println("Finalized object with trait: " + trait + " " +
     * toString()); super.finalize(); }; /*
     */

}
