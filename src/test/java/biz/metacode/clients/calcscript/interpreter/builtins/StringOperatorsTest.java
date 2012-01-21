package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.execution.ScriptExecutionException;

import org.junit.Test;

public class StringOperatorsTest extends OperatorTestBase {

    @Test
    public void splitAroundMatchesNonEmpty() throws ScriptExecutionException, InterruptedException {
        register("%", new OrderedDispatcher("%"));
        register("%_string_string", StringOperators.SPLIT_AROUND_MATCHES_NONEMPTY);
        assertEval("[a df]", "'assdfs' 's'%");
    }


    @Test
    public void eval() throws ScriptExecutionException, InterruptedException {
        register("+", ArithmeticOperators.ADDITION);
        register("~", new SingleDispatcher("~"));
        register("~_string", StringOperators.EVAL);
        assertEval("3", "'1 2+'~");
    }
}
