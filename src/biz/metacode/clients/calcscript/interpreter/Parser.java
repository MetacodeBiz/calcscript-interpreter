
package biz.metacode.clients.calcscript.interpreter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Parser implements Iterator<Visitable>, Iterable<Visitable> {

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

    public Visitable parseTopLevel() {
        if (":".equals(current)) {
            return this.parseAssignment();
        } else if ("{".equals(current)) {
            return this.parseBlock();
        } else if ("#".equals(current.charAt(0))) {
            this.nextConstruct();
            return this.parseTopLevel();
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
        while ("}".equals(this.current)) {
            members.add(this.parseTopLevel());
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
        return this.parseTopLevel();
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
