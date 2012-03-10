
package biz.metacode.calcscript.interpreter;

import java.io.Serializable;

/**
 * Base interface for object that can be invoked. All {@link Value} objects can
 * be invoked but invoking most of them just places them onto the stack. Blocks
 * are special because invoking them runs code associated with the block. Custom
 * defined functions that implement this interface may execute any kind of
 * logic.
 * <p>
 * All invocable objects must support serialization because they may be stored
 * in memory or on disk and later retrieved.
 */
public interface Invocable extends Serializable {

    /**
     * Executed when current object has been invoked by name.
     *
     * @param context Execution context of current program.
     * @throws InterruptedException If script execution was interrupted.
     */
    void invoke(ExecutionContext context) throws InterruptedException;
}
