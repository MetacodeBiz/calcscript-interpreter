
package biz.metacode.calcscript.interpreter.source;

import biz.metacode.calcscript.interpreter.ExecutionContext;
import biz.metacode.calcscript.interpreter.Value;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Block of code consisting of variables, assignments and other blocks. When
 * block is encountered during execution it is pushed onto the stack and it is
 * not executed.
 */
class Block extends Value implements Expression, Serializable {

    private static final long serialVersionUID = -2750855771404873550L;

    private final List<Expression> members;

    Block(final List<Expression> members) {
        this.members = members;
    }

    public void evaluate(final ExecutionContext context) {
        context.push(this);
    }

    @Override
    public void invoke(final ExecutionContext context) throws InterruptedException {
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
    public Type getType() {
        return Type.BLOCK;
    }

    @Override
    public boolean toBoolean() {
        return !members.isEmpty();
    }

    public Block concatenate(final Block other) {
        List<Expression> exprs = new ArrayList<Expression>(this.members.size()
                + other.members.size());
        exprs.addAll(this.members);
        exprs.addAll(other.members);
        return new Block(exprs);
    }

}
