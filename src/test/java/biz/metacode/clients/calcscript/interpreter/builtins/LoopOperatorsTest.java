package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.execution.ScriptExecutionException;

import org.junit.Test;

public class LoopOperatorsTest extends OperatorTestBase {

    @Test
    public void doLoop() throws ScriptExecutionException, InterruptedException {
        register(".", StackOperators.DUPLICATE);
        register("-", ArithmeticOperators.SUBSTRACTION);
        register("do", LoopOperators.DO);
        assertEval("4 3 2 1 0 0", "5{1-..}do");
    }

    @Test
    public void times() throws ScriptExecutionException, InterruptedException {
        register("*", new OrderedDispatcher("*"));
        register("*_number_number", ArithmeticOperators.MULTIPLICATION);
        register("*_block_number", LoopOperators.TIMES);
        assertEval("64", "2 {2*} 5*");
    }

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
        assertEval("1 2 3 4 5 6 6", "0{.5>}{1+.}until");
    }
}
