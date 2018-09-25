
package biz.metacode.calcscript.interpreter.source;

import biz.metacode.calcscript.interpreter.ExecutionContext;
import biz.metacode.calcscript.interpreter.ScriptExecutionException;
import biz.metacode.calcscript.interpreter.ValueMissingException;

import java.io.Serializable;

/**
 * Assignment of the top of stack to named variable. Note that this does not pop
 * the stack.
 */
class Assignment implements Expression, Serializable {

    private static final long serialVersionUID = 4922210833587000856L;

    private final String targetVariableName;

    Assignment(final String targetVariableName) {
        this.targetVariableName = targetVariableName;
    }

    public String getTargetVariableName() {
        return targetVariableName;
    }

    public void evaluate(final ExecutionContext context) {
        try {
            context.write(targetVariableName, context.peek());
        } catch (ValueMissingException e) {
            throw new ScriptExecutionException(":", "1:a", e);
        }
    }

}
