
package biz.metacode.clients.calcscript.interpreter;

import java.util.List;

public class Block implements Visitable, Executable {

    private final List<Visitable> members;

    public Block(List<Visitable> members) {
        this.members = members;
    }

    @Override
    public void visit(Stack stack, Memory memory) {
        stack.push(this);
    }

    @Override
    public void execute(Stack stack, Memory memory) {
        for (Visitable visitable : members) {
            visitable.visit(stack, memory);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        for(Visitable member : members) {
            String memberString = member.toString();
            if (memberString != null && !"".equals(memberString)) {
                builder.append(memberString);
            }
        }
        return builder.append('}').toString();
    }

}
