
package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.execution.ScriptExecutionException;

import org.junit.Test;

public class BooleanOperatorsTest extends OperatorTestBase {

    @Test
    public void or() throws ScriptExecutionException, InterruptedException {
        register("/", ArithmeticOperators.DIVISION);
        register("or", BooleanOperators.OR);
        assertEval("5", "5 {1 0/} or");
    }

    @Test
    public void and() throws ScriptExecutionException, InterruptedException {
        register("+", ArithmeticOperators.ADDITION);
        register("and", BooleanOperators.AND);
        assertEval("2", "5 {1 1+} and");
    }

    @Test
    public void xor() throws ScriptExecutionException, InterruptedException {
        register("[", StackOperators.LEFT_SQUARE_BRACE);
        register("]", StackOperators.RIGHT_SQUARE_BRACE);
        register("xor", BooleanOperators.XOR);
        assertEval("[3]", "0 [3] xor");
    }

    @Test
    public void xor2() throws ScriptExecutionException, InterruptedException {
        register("[", StackOperators.LEFT_SQUARE_BRACE);
        register("]", StackOperators.RIGHT_SQUARE_BRACE);
        register("xor", BooleanOperators.XOR);
        assertEval("0", "2 [3] xor");
    }

    @Test
    public void ifOperator() throws ScriptExecutionException, InterruptedException {
        register(".", StackOperators.DUPLICATE);
        register("if", BooleanOperators.IF);
        assertEval("2", "1 2 3 if");
        assertEval("1 1", "0 2 {1.} if");
    }

}
