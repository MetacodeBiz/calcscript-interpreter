
package biz.metacode.clients.calcscript.interpreter.execution;

public class EngineTestHelper {

    private final Context context;

    public EngineTestHelper(Context context) {
        this.context = context;
    }

    public int getCurrentNumericPoolSize() {
        return ((NumericPool) context.getPool(Numeric.class)).internalGetPooledObjectsCount();
    }

    public int getCurrentArrayPoolSize() {
        return ((ArrayPool) context.getPool(Array.class)).internalGetPooledObjectsCount();
    }

    public int getCurrentTextPoolSize() {
        return ((TextPool) context.getPool(Text.class)).internalGetPooledObjectsCount();
    }

    public void setTrait(String trait) {
        context.setTrait(trait);
    }
}
