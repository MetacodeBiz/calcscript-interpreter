
package biz.metacode.clients.calcscript.interpreter;

/**
 * Base interface for all constructs produced by Parser.
 */
interface Expression {
    /**
     * Invoked when engine encounters this expression during program execution.
     *
     * @param context Execution context.
     */
    void evaluate(ExecutionContext context) throws InterruptedException;
}
