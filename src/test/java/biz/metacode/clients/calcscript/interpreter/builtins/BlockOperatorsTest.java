package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.execution.ScriptExecutionException;

import org.junit.Test;

public class BlockOperatorsTest extends OperatorTestBase {

    @Test
    public void unfold() throws ScriptExecutionException, InterruptedException {
        register("<", ComparisonOperators.LESS_THAN);
        register(".", StackOperators.DUPLICATE);
        register("@", StackOperators.ROT3);
        register("+", ArithmeticOperators.ADDITION);
        register("/", BlockOperators.UNFOLD);
        assertEval("89 [1 1 2 3 5 8 13 21 34 55 89]", "0 1 {100<} { .@+ } /");
    }
}
