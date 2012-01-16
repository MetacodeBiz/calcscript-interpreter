
package biz.metacode.clients.calcscript.interpreter;

import java.util.ArrayList;
import java.util.List;

/**
 * Micro-compiler that transforms a {@link String} into executable program.
 */
public final class Program implements Invocable {

    private static final long serialVersionUID = 6456163484383557746L;

    private final CharSequence source;

    public Program(CharSequence source) {
        this.source = source;
    }

    public void invoke(ExecutionContext context) {
        Parser parser = new Parser(source);
        for (Expression visitable : parser) {
            visitable.evaluate(context);
        }
    }

    public List<String> extractTargetVariableNames() {
        List<String> variableNames = new ArrayList<String>();
        Parser parser = new Parser(source);
        for (Expression visitable : parser) {
            if (visitable instanceof Assignment) {
                variableNames.add(((Assignment) visitable).getTargetVariableName());
            }
        }
        return variableNames;
    }

}
