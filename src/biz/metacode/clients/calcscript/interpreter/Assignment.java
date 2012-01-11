
package biz.metacode.clients.calcscript.interpreter;

public class Assignment implements Visitable {

    private final String targetVariableName;

    public Assignment(String targetVariableName) {
        this.targetVariableName = targetVariableName;
    }

    public String getTargetVariableName() {
        return targetVariableName;
    }

    @Override
    public void visit(ExecutionContext context) {
        context.write(targetVariableName, context.peek());
    }

}
