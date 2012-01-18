
package biz.metacode.clients.calcscript.interpreter;

import java.io.Serializable;
import java.util.List;

/**
 * Block of code consisting of variables, assignments and other blocks. When
 * block is encountered during execution it is pushed onto the stack and it is
 * not executed.
 */
public class Block extends Value implements Expression, Serializable {

    private static final long serialVersionUID = -2750855771404873550L;

    private final List<Expression> members;

    public Block(List<Expression> members) {
        this.members = members;
    }

    public void evaluate(ExecutionContext context) {
        context.push(this);
    }

    @Override
    public void invoke(ExecutionContext context) throws InterruptedException {
        for (Expression visitable : members) {
            visitable.evaluate(context);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        for (Expression member : members) {
            String memberString = member.toString();
            if (memberString != null && !"".equals(memberString)) {
                builder.append(memberString);
            }
        }
        return builder.append('}').toString();
    }

    @Override
    public int getPriority() {
        return 4;
    }

    /**
     * Returns this as a duplicate because {@link Block}s are immutable.
     */
    @Override
    public Value duplicate() {
        return this;
    }

    @Override
    public String getTypeName() {
        return "block";
    }

}
