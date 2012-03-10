
package biz.metacode.calcscript.interpreter;

/**
 * Operator that can provide information about itself.
 */
public interface SelfDescribing {

    /**
     * Return an example of usage of this operator. Returned string can contain
     * {@literal <name>} and it will be replaced with operator's name.
     *
     * @return Example usage or {@code null} if usage is not available.
     */
    String getExampleUsage();
}
