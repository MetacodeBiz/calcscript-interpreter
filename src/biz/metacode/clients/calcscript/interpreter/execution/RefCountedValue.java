
package biz.metacode.clients.calcscript.interpreter.execution;

import biz.metacode.clients.calcscript.interpreter.ExecutionContext;
import biz.metacode.clients.calcscript.interpreter.Value;

public abstract class RefCountedValue extends Value {

    private static final long serialVersionUID = 6994581318461354474L;

    private int refCounter = 1;

    public void invoke(ExecutionContext context) {
        super.invoke(context);
        refCounter++;
    }

    public void acquire() {
        refCounter++;
    }

    public void release() {
        --refCounter;
        assert refCounter >= 0 : "Released object more than once!";
        if (refCounter == 0) {
            relinquish();
        }
    }

    protected abstract void relinquish();

}
