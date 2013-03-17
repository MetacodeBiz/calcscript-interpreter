
package biz.metacode.calcscript.interpreter;

import java.util.List;

/**
 * Represents array that is pooled.
 */
public interface SharedArray extends List<Value> {

    /**
     * Releases current object into array pool. Always execute this method after
     * interacting with the array if it was returned from
     * {@link ExecutionContext#pop()} or
     * {@link biz.metacode.calcscript.interpreter.execution.Engine#execute(CharSequence)}
     * and never access the array after executing {@link #release()}.
     */
    void release();
}
