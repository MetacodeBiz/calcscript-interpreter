package biz.metacode.clients.calcscript.interpreter;

import java.util.ArrayList;
import java.util.List;

public final class Program implements Executable {

    private final CharSequence source;

    public Program(CharSequence source) {
        this.source = source;
    }

    @Override
    public void execute(Stack stack, Memory memory) {
        Parser parser = new Parser(source);
        for (Visitable visitable : parser) {
            visitable.visit(stack, memory);
        }
    }

    public List<String> extractTargetVariableNames() {
        List<String> variableNames = new ArrayList<String>();
        Parser parser = new Parser(source);
        for (Visitable visitable : parser) {
            if (visitable instanceof Assignment) {
                variableNames.add(((Assignment) visitable).getTargetVariableName());
            }
        }
        return variableNames;
    }

}
