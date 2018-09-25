
package biz.metacode.calcscript.interpreter.execution;

class EngineTestHelper {

    private final Context context;

    EngineTestHelper(final Context context) {
        this.context = context;
    }

    public int getCurrentNumericPoolSize() {
        return ((NumericPool) context.getPool(Numeric.class))
                .internalGetPooledObjectsCount();
    }

    public int getCurrentArrayPoolSize() {
        return ((ArrayPool) context.getPool(Array.class)).internalGetPooledObjectsCount();
    }

    public int getCurrentTextPoolSize() {
        return ((TextPool) context.getPool(Text.class)).internalGetPooledObjectsCount();
    }

    public int getNumericAllocationBalance() {
        return ((NumericPool) context.getPool(Numeric.class))
                .internalGetAllocationBalance();
    }

    public int getArrayAllocationBalance() {
        return ((ArrayPool) context.getPool(Array.class)).internalGetAllocationBalance();
    }

    public int getTextAllocationBalance() {
        return ((TextPool) context.getPool(Text.class)).internalGetAllocationBalance();
    }

    public void resetAllocationBalance() {
        ((NumericPool) context.getPool(Numeric.class)).internalResetAllocationBalance();
        ((ArrayPool) context.getPool(Array.class)).internalResetAllocationBalance();
        ((TextPool) context.getPool(Text.class)).internalResetAllocationBalance();
    }

    public void setTrait(final String trait) {
        context.setTrait(trait);
    }
}
