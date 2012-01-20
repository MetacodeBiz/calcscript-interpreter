package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.execution.ScriptExecutionException;

import org.junit.Test;

public class ArithmeticOperatorsTest extends OperatorTestBase {

    @Test
    public void unfold() throws ScriptExecutionException, InterruptedException {
        register("?", ArithmeticOperators.POWER);
        assertEval("256", "2 8?");
    }
}
