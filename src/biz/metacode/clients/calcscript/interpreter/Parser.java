
package biz.metacode.clients.calcscript.interpreter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class Parser implements Iterator<Visitable>, Iterable<Visitable> {

    private final Iterator<String> tokens;

    private String current;

    public Parser(CharSequence source) {
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

    private Visitable parseTopLevel() {
        if (current == null) {
            return null;
        }
        if (":".equals(current)) {
            return this.parseAssignment();
        } else if ("{".equals(current)) {
            return this.parseBlock();
        } else if ("#".equals(current.charAt(0))) {
            this.nextConstruct();
            Visitable next = this.parseTopLevel();
            if (next == null) {
                throw new SyntaxException("End of script");
            }
            return next;
        } else {
            return this.parseVariable();
        }
    }

    private Visitable parseVariable() {
        Variable variable = new Variable(current);
        this.nextConstruct();
        return variable;
    }

    private Visitable parseBlock() {
        List<Visitable> members = new ArrayList<Visitable>();
        this.nextConstruct();
        while (!"}".equals(this.current)) {
            Visitable member = this.parseTopLevel();
            if (member == null) {
                throw new SyntaxException("} missing.");
            }
            members.add(member);
        }
        this.nextConstruct();
        return new Block(members);
    }

    private Visitable parseAssignment() {
        this.nextConstruct();
        Assignment assignment = new Assignment(current);
        this.nextConstruct();
        return assignment;
    }

    @Override
    public boolean hasNext() {
        return this.current != null;
    }

    @Override
    public Visitable next() {
        Visitable next = this.parseTopLevel();
        if (next == null) {
            throw new SyntaxException("End of script");
        }
        return next;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Visitable> iterator() {
        return this;
    }
}
