
package biz.metacode.clients.calcscript.interpreter.builtins.test;

import biz.metacode.clients.calcscript.interpreter.builtins.ArithmeticOperators;
import biz.metacode.clients.calcscript.interpreter.builtins.BooleanOperators;
import biz.metacode.clients.calcscript.interpreter.builtins.ComparisonOperators;
import biz.metacode.clients.calcscript.interpreter.builtins.StackOperators;
import biz.metacode.clients.calcscript.interpreter.execution.EngineTestBase;
import biz.metacode.clients.calcscript.interpreter.execution.ScriptExecutionException;

import org.junit.Test;

public class BooleanOperatorsTest extends EngineTestBase {

    @Test
    public void not() throws ScriptExecutionException, InterruptedException {
        register("!", BooleanOperators.NOT);
        assertEval("0", "5!");
        assertEval("1", "''!");
    }

    @Test
    public void or() throws ScriptExecutionException, InterruptedException {
        register("/", ArithmeticOperators.DIVISION);
        register("or", BooleanOperators.OR);
        assertEval("5", "5 {1 0/} or");
        assertEval("2", "0 {1 1+} or");
    }

    @Test
    public void and() throws ScriptExecutionException, InterruptedException {
        register("+", ArithmeticOperators.ADDITION);
        register("and", BooleanOperators.AND);
        assertEval("2", "5 {1 1+} and");
        assertEval("0", "0 {1 1+} and");
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

    @Test
    public void equals() throws ScriptExecutionException, InterruptedException {
        register("=", ComparisonOperators.EQUALS);
        assertEval("1", "3 3=");
        assertEval("0", "'3'3=");
    }

}
