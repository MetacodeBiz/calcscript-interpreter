
package biz.metacode.calcscript.interpreter.source;

import biz.metacode.calcscript.interpreter.ExecutionContext;

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
