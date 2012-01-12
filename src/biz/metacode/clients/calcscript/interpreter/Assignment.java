
package biz.metacode.clients.calcscript.interpreter;

/**
 * Assignment of the top of stack to named variable. Note that this does not pop
 * the stack.
 */
class Assignment implements Expression {

    private final String targetVariableName;

    public Assignment(String targetVariableName) {
        this.targetVariableName = targetVariableName;
    }

    public String getTargetVariableName() {
        return targetVariableName;
    }

    @Override
    public void evaluate(ExecutionContext context) {
        context.write(targetVariableName, context.peek());
    }

}
