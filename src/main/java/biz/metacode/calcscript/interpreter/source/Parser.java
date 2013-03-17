
package biz.metacode.calcscript.interpreter.source;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class Parser implements Iterator<Expression>, Iterable<Expression> {

    private final Iterator<String> tokens;

    private String current;

    public Parser(final CharSequence source) {
        this.tokens = (new Lexer(source)).iterator();
        this.nextConstruct();
    }

    private void nextConstruct() {
        if (!this.tokens.hasNext()) {
            this.current = null;
        } else {
            this.current = this.tokens.next();
        }
    }

    private Expression parseTopLevel() {
        if (current == null) {
            return null;
        }
        if (":".equals(current)) {
            return this.parseAssignment();
        } else if ("{".equals(current)) {
            return this.parseBlock();
        } else if ('#' == current.charAt(0)) {
            this.nextConstruct();
            Expression next = this.parseTopLevel();
            if (next == null) {
                throw new SyntaxException("End of script");
            }
            return next;
        } else {
            return this.parseVariable();
        }
    }

    private Expression parseVariable() {
        Variable variable = new Variable(current);
        this.nextConstruct();
        return variable;
    }

    private Expression parseBlock() {
        List<Expression> members = new ArrayList<Expression>();
        this.nextConstruct();
        while (!"}".equals(this.current)) {
            Expression member = this.parseTopLevel();
            if (member == null) {
                throw new UnclosedBlockException();
            }
            members.add(member);
        }
        this.nextConstruct();
        return new Block(members);
    }

    private Expression parseAssignment() {
        this.nextConstruct();
        if (current == null) {
            throw new VariableNameExpectedException();
        }
        if ("}".equals(current) || ":".equals(current) || "#".equals(current)) {
            throw new VariableNameExpectedException();
        }
        Assignment assignment = new Assignment(current);
        this.nextConstruct();
        return assignment;
    }

    public boolean hasNext() {
        return this.current != null;
    }

    public Expression next() {
        Expression next = this.parseTopLevel();
        if (next == null) {
            throw new SyntaxException("End of script");
        }
        return next;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public Iterator<Expression> iterator() {
        return this;
    }
}
