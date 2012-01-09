package biz.metacode.clients.calcscript.interpreter;

import biz.metacode.clients.calcscript.interpreter.execution.Context;

import java.util.ArrayList;
import java.util.List;

public final class Program implements Executable {

    private final CharSequence source;

    public Program(CharSequence source) {
        this.source = source;
    }

    @Override
    public void execute(Context context) {
        Parser parser = new Parser(source);
        for (Visitable visitable : parser) {
            visitable.visit(context);
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
