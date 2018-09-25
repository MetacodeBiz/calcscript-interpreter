package biz.metacode.calcscript.interpreter.builtins;

/**
 * Provides common utilities for operator implementations.
 */
public final class Operators {

    private Operators() {
        //
    }

    /**
     * Check if script execution should be interrupted. This method should be called
     * within loops or any kind of code that may never finish.
     *
     * @throws InterruptedException If script execution should be interrupted.
     */
    public static void interruptionPoint() throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
    }
}
