package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.execution.ScriptExecutionException;

import org.junit.Test;

public class ArrayOperatorsTest extends OperatorTestBase {

    @Test
    public void everyOther() throws ScriptExecutionException, InterruptedException {
        register("[", StackOperators.LEFT_SQUARE_BRACE);
        register("]", StackOperators.RIGHT_SQUARE_BRACE);
        register("%", new OrderedDispatcher("%"));
        register("%_array_number", ArrayOperators.EVERY_NTH_ELEMENT);
        assertEval("[1 3 5]", "[1 2 3 4 5] 2%");
        assertEval("[5 4 3 2 1]", "[1 2 3 4 5] -1%");
    }
}
