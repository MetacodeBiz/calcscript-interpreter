
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

    @Test
    public void setUnion() throws ScriptExecutionException, InterruptedException {
        register("[", StackOperators.LEFT_SQUARE_BRACE);
        register("]", StackOperators.RIGHT_SQUARE_BRACE);
        register("|", new OrderedDispatcher("|"));
        register("|_array_array", ArrayOperators.UNION);
        assertEval("[1 3 5]", "[5 1][3]|");
    }

    @Test
    public void setIntersection() throws ScriptExecutionException, InterruptedException {
        register("[", StackOperators.LEFT_SQUARE_BRACE);
        register("]", StackOperators.RIGHT_SQUARE_BRACE);
        register("&", new OrderedDispatcher("&"));
        register("&_array_array", ArrayOperators.INTERSECTION);
        assertEval("[1]", "[1 1 2 2][1 3]&");
    }

    @Test
    public void setSymmetricDifference() throws ScriptExecutionException, InterruptedException {
        register("[", StackOperators.LEFT_SQUARE_BRACE);
        register("]", StackOperators.RIGHT_SQUARE_BRACE);
        register("^", new OrderedDispatcher("^"));
        register("^_array_array", ArrayOperators.SYMMETRIC_DIFFERENCE);
        assertEval("[2 3]", "[1 1 2 2][1 3]^");
    }

    @Test
    public void indexLessThan() throws ScriptExecutionException, InterruptedException {
        register("[", StackOperators.LEFT_SQUARE_BRACE);
        register("]", StackOperators.RIGHT_SQUARE_BRACE);
        register("<", new OrderedDispatcher("<"));
        register("<_array_number", ArrayOperators.INDEX_LESS_THAN);
        assertEval("[1 2]", "[1 2 3] 2 <");
    }

    @Test
    public void indexGreaterThan() throws ScriptExecutionException, InterruptedException {
        register("[", StackOperators.LEFT_SQUARE_BRACE);
        register("]", StackOperators.RIGHT_SQUARE_BRACE);
        register(">", new OrderedDispatcher(">"));
        register(">_array_number", ArrayOperators.INDEX_GREATER_THAN);
        assertEval("[3]", "[1 2 3] 2 >");
    }
}
