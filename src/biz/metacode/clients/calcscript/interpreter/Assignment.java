
package biz.metacode.clients.calcscript.interpreter;

public class Assignment implements Visitable {

    private final String targetVariableName;

    public Assignment(String targetVariableName) {
        this.targetVariableName = targetVariableName;
    }

    @Override
    public void visit(Stack stack, Memory memory) {
        memory.write(targetVariableName, stack.peek());
    }

}
