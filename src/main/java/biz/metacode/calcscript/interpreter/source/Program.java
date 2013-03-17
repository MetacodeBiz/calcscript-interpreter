
package biz.metacode.calcscript.interpreter.source;

import biz.metacode.calcscript.interpreter.ExecutionContext;
import biz.metacode.calcscript.interpreter.Invocable;
import biz.metacode.calcscript.interpreter.Value;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Converts a script source into executable program. This is a low-level
 * interpreter that requires {@link ExecutionContext}. See
 * {@link biz.metacode.calcscript.interpreter.execution.Engine} for a complete
 * execution engine.
 * <p>
 * Because this class implements {@link Invocable} it can be used to store bare
 * string scripts in engine memory using
 * {@link biz.metacode.calcscript.interpreter.execution.Engine#register(String, Invocable)}.
 */
public final class Program implements Invocable {

    private static final long serialVersionUID = 6456163484383557746L;

    private final CharSequence source;

    /**
     * Creates new program from given script source.
     *
     * @param source Script source.
     */
    public Program(@Nonnull final CharSequence source) {
        this.source = source;
    }

    /**
     * Parses and executes script.
     *
     * @param context Context of execution.
     * @throws InterruptedException If script execution is interrupted.
     */
    public void invoke(final ExecutionContext context) throws InterruptedException {
        Parser parser = new Parser(source);
        for (Expression visitable : parser) {
            visitable.evaluate(context);
        }
    }

    /**
     * Returns a list of variable names that are assigned to in this script.
     *
     * @return List of variable names.
     */
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

    /**
     * Concatenates two executables into one.
     *
     * @param first First executable.
     * @param second Second executable.
     * @return Combined executable.
     */
    public static Value concatenate(final Value first, final Value second) {
        if (first instanceof Block && second instanceof Block) {
            return ((Block) first).concatenate((Block) second);
        }
        throw new IllegalArgumentException("Cannot concatenate those invocables.");
    }

    /**
     * Creates an executable that will when called will invoke variables given
     * as parameters.
     *
     * @param variableNames Names of variables to be invoked.
     * @return Executable.
     */
    public static Value createInvocable(final String... variableNames) {
        List<Expression> variables = new ArrayList<Expression>(variableNames.length);
        for (String variableName : variableNames) {
            variables.add(new Variable(variableName));
        }
        return new Block(variables);
    }

}
