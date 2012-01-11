
package biz.metacode.clients.calcscript.interpreter;

import biz.metacode.clients.calcscript.interpreter.execution.Context;
import biz.metacode.clients.calcscript.interpreter.execution.Value;

import java.util.List;

public class Block extends Value implements Visitable {

    private static final long serialVersionUID = -2750855771404873550L;

    private final List<Visitable> members;

    public Block(List<Visitable> members) {
        this.members = members;
    }

    @Override
    public void visit(Context context) {
        context.push(this);
    }

    @Override
    public void invoke(Context context) {
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
