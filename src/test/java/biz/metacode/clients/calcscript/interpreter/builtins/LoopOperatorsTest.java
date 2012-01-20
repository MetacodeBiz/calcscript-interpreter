package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.execution.ScriptExecutionException;

import org.junit.Test;

public class LoopOperatorsTest extends OperatorTestBase {

    @Test
    public void whileLoop() throws ScriptExecutionException, InterruptedException {
        register(".", StackOperators.DUPLICATE);
        register("-", ArithmeticOperators.SUBSTRACTION);
        register("while", LoopOperators.WHILE);
        assertEval("4 3 2 1 0 0", "5{.}{1-.}while");
    }

    @Test
    public void untilLoop() throws ScriptExecutionException, InterruptedException {
        register(".", StackOperators.DUPLICATE);
        register("-", ArithmeticOperators.SUBSTRACTION);
        register("until", LoopOperators.UNTIL);
        assertEval("5", "5{.}{1-.}until");
    }
}
