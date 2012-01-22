
package biz.metacode.calcscript.interpreter;

import java.util.List;

/**
 * Represents array that is pooled.
 */
public interface SharedArray extends List<Value> {

    /**
     * Releases current object into array pool. Always execute this method after
     * interacting with the array and never access the array after executing
     * {@link #release()}.
     */
    void release();
}
