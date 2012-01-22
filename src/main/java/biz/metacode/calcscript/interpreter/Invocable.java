
package biz.metacode.calcscript.interpreter;

import java.io.Serializable;

/**
 * Base interface for object that can be invoked. All {@link Value} objects can
 * be invoked but invoking most of them places them on the stack. Blocks are
 * special because invoking them runs code associated with the block.
 * <p>
 * All invocable objects must support serialization because they may be stored
 * in memory or on disk and later retrieved.
 */
public interface Invocable extends Serializable {

    /**
     * Executed when current object has been invoked by name.
     *
     * @param context Execution context of current program
     */
    void invoke(ExecutionContext context) throws InterruptedException;
}
