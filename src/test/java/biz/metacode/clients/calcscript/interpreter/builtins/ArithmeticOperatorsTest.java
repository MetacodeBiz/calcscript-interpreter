package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.execution.ScriptExecutionException;

import org.junit.Test;

public class ArithmeticOperatorsTest extends OperatorTestBase {

    @Test
    public void unfold() throws ScriptExecutionException, InterruptedException {
        register("?", ArithmeticOperators.POWER);
        assertEval("256", "2 8?");
    }

    @Test
    public void addition() throws ScriptExecutionException, InterruptedException {
        register("+", ArithmeticOperators.ADDITION);
        assertEval("12", "5 7+");
    }

    @Test
    public void substraction() throws ScriptExecutionException, InterruptedException {
        register("+", ArithmeticOperators.ADDITION);
        register("-", ArithmeticOperators.SUBSTRACTION);
        assertEval("1 -1", "1 2-3+");
        assertEval("1 -1", "1 2 -3+");
        assertEval("2", "1 2- 3+");
    }

    @Test
    public void multiplication() throws ScriptExecutionException, InterruptedException {
        register("*", ArithmeticOperators.MULTIPLICATION);
        assertEval("8", "2 4*");
    }

    @Test
    public void division() throws ScriptExecutionException, InterruptedException {
        register("/", ArithmeticOperators.DIVISION);
        assertEval("2", "6 3 /");
    }

    @Test
    public void decrement() throws ScriptExecutionException, InterruptedException {
        register("(", ArithmeticOperators.DECREMENT);
        assertEval("4", "5(");
    }

    @Test
    public void increment() throws ScriptExecutionException, InterruptedException {
        register(")", ArithmeticOperators.INCREMENT);
        assertEval("6", "5)");
    }

    @Test
    public void random() throws ScriptExecutionException, InterruptedException {
        register("rand", ArithmeticOperators.RANDOM);
        assertEval("0", "1rand");
    }

    @Test
    public void abs() throws ScriptExecutionException, InterruptedException {
        register("abs", MathOperators.ABSOLUTE);
        assertEval("4", "-4abs");
        assertEval("4", "4abs");
    }
}
