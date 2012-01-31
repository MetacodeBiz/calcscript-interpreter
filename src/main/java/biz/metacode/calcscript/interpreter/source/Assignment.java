
package biz.metacode.calcscript.interpreter.source;

import biz.metacode.calcscript.interpreter.ExecutionContext;
import biz.metacode.calcscript.interpreter.ValueMissingException;

import java.io.Serializable;

/**
 * Assignment of the top of stack to named variable. Note that this does not pop
 * the stack.
 */
class Assignment implements Expression, Serializable {

    private static final long serialVersionUID = 4922210833587000856L;

    private final String targetVariableName;

    public Assignment(String targetVariableName) {
        this.targetVariableName = targetVariableName;
    }

    public String getTargetVariableName() {
        return targetVariableName;
    }

    public void evaluate(ExecutionContext context) {
        try {
            context.write(targetVariableName, context.peek());
        } catch (ValueMissingException e) {
            e.setOperatorName(":");
            e.setExample("1:a");
            throw e;
        }
    }

}
