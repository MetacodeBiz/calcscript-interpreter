
package biz.metacode.clients.calcscript.interpreter;

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
        context.write(targetVariableName, context.peek());
    }

}
