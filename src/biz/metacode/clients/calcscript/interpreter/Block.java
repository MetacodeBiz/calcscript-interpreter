
package biz.metacode.clients.calcscript.interpreter;

import java.util.List;

public class Block extends Value implements Visitable {

    private static final long serialVersionUID = -2750855771404873550L;

    private final List<Visitable> members;

    public Block(List<Visitable> members) {
        this.members = members;
    }

    @Override
    public void visit(ExecutionContext context) {
        context.push(this);
    }

    @Override
    public void invoke(ExecutionContext context) {
        for (Visitable visitable : members) {
            visitable.visit(context);
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

    @Override
    protected int getPriority() {
        return 4;
    }

}
