package biz.metacode.calcscript.interpreter.builtins.test;

import biz.metacode.calcscript.interpreter.ScriptExecutionException;
import biz.metacode.calcscript.interpreter.builtins.ArithmeticOperators;
import biz.metacode.calcscript.interpreter.builtins.MathOperators;
import biz.metacode.calcscript.interpreter.builtins.StackOperators;
import biz.metacode.calcscript.interpreter.execution.EngineTestBase;

import org.junit.Test;

public class ArithmeticOperatorsTest extends EngineTestBase {

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
    public void negativeRandom() throws ScriptExecutionException, InterruptedException {
        register("rand", ArithmeticOperators.RANDOM);
        assertEval("-0", "-1rand");
    }

    @Test
    public void smallRandom() throws ScriptExecutionException, InterruptedException {
        register("rand", ArithmeticOperators.RANDOM);
        String result = eval("-0.5rand");
        Double.parseDouble(result);
    }

    @Test
    public void sinus() throws ScriptExecutionException, InterruptedException {
        register("sin", ArithmeticOperators.SINUS);
        assertEval("0.841", "1sin");
    }

    @Test
    public void cosinus() throws ScriptExecutionException, InterruptedException {
        register("cos", ArithmeticOperators.COSINUS);
        assertEval("0.54", "1cos");
    }

    @Test
    public void abs() throws ScriptExecutionException, InterruptedException {
        register("abs", MathOperators.ABSOLUTE);
        assertEval("4", "-4abs");
        assertEval("4", "4abs");
    }

    @Test
    public void convertNumber() throws ScriptExecutionException, InterruptedException {
        register("base", MathOperators.CONVERT_NUMBER_BASE);
        assertEval("[1 1 0]", "6 2 base");
        assertEval("[2 2]", "8 3 base");
        assertEval("[1 1 1]", "3 1 base");
        assertEval("[15 15]", "255 16 base");
    }

    @Test
    public void convertArray() throws ScriptExecutionException, InterruptedException {
        register("base", MathOperators.CONVERT_ARRAY_BASE);
        register("[", StackOperators.MARK_STACK_SIZE);
        register("]", StackOperators.SLICE_STACK);
        assertEval("6", "2[1 1 0]base");
        assertEval("6", "2[1 \"1\" {}]base");
        assertEval("8", "3[2 2]base");
        assertEval("2", "1[2 2]base");
        assertEval("255", "16[15 15]base");
    }

    @Test
    public void selfDescribing() {
        assertDescriptions(MathOperators.values());
    }
}
