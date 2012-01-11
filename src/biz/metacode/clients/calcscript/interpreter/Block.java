
package biz.metacode.clients.calcscript.interpreter;

import java.util.List;

/**
 * Block of code consisting of variables, assignments and other blocks. When
 * block is encountered during execution it is pushed onto the stack and it is
 * not executed.
 */
class Block extends Value implements Expression {

    private static final long serialVersionUID = -2750855771404873550L;

    private final List<Expression> members;

    public Block(List<Expression> members) {
        this.members = members;
    }

    @Override
    public void hit(ExecutionContext context) {
        context.push(this);
    }

    @Override
    public void invoke(ExecutionContext context) {
        for (Expression visitable : members) {
            visitable.hit(context);
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
    protected int getPriority() {
        return 4;
    }

    /**
     * Returns this as a duplicate because {@link Block}s are immutable.
     */
    @Override
    public Value duplicate() {
        return this;
    }

}
