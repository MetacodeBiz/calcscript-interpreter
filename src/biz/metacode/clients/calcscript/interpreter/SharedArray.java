
package biz.metacode.clients.calcscript.interpreter;

import java.util.Collection;

/**
 * Represents array that is pooled.
 */
public interface SharedArray extends Collection<Value> {

    /**
     * Releases current object into array pool. Always execute this method after
     * interacting with the array and never access the array after executing
     * {@link #release()}.
     */
    void release();
}
