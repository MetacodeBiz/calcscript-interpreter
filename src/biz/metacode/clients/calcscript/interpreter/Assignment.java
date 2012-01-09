
package biz.metacode.clients.calcscript.interpreter;

import biz.metacode.clients.calcscript.interpreter.execution.Context;

public class Assignment implements Visitable {

    private static final long serialVersionUID = 9115138722566546001L;

    private final String targetVariableName;

    public Assignment(String targetVariableName) {
        this.targetVariableName = targetVariableName;
    }

    public String getTargetVariableName() {
        return targetVariableName;
    }

    @Override
    public void visit(Context context) {
        context.write(targetVariableName, context.peek());
    }

}
