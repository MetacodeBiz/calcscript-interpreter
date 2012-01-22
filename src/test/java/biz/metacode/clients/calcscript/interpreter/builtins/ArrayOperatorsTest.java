
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
        assertEval("", "[1 2 3 4 5] 0%");
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

    @Test
    public void getElement() throws ScriptExecutionException, InterruptedException {
        register("[", StackOperators.LEFT_SQUARE_BRACE);
        register("]", StackOperators.RIGHT_SQUARE_BRACE);
        register("=", new OrderedDispatcher("="));
        register("=_array_number", ArrayOperators.GET_ELEMENT);
        assertEval("3", "[1 2 3] 2 =");
        assertEval("", "[1 2 3] 5 =");
    }

    @Test
    public void createArray() throws ScriptExecutionException, InterruptedException {
        register(",", new SingleDispatcher(","));
        register(",_number", ArrayOperators.CREATE_ARRAY);
        assertEval("[0 1 2 3 4 5 6 7 8 9]", "10,");
    }

    @Test
    public void indexOf() throws ScriptExecutionException, InterruptedException {
        register("[", StackOperators.LEFT_SQUARE_BRACE);
        register("]", StackOperators.RIGHT_SQUARE_BRACE);
        register("?", new OrderedDispatcher("?"));
        register("?_array_number", ArrayOperators.INDEX_OF);
        assertEval("2", "5 [4 3 5 1] ?");
    }

    @Test
    public void uncons() throws ScriptExecutionException, InterruptedException {
        register("[", StackOperators.LEFT_SQUARE_BRACE);
        register("]", StackOperators.RIGHT_SQUARE_BRACE);
        register("(", new SingleDispatcher("("));
        register("(_array", ArrayOperators.UNCONS);
        assertEval("[2 3] 1", "[1 2 3](");
    }

    @Test
    public void unconsRight() throws ScriptExecutionException, InterruptedException {
        register("[", StackOperators.LEFT_SQUARE_BRACE);
        register("]", StackOperators.RIGHT_SQUARE_BRACE);
        register(")", new SingleDispatcher(")"));
        register(")_array", ArrayOperators.UNCONS_RIGHT);
        assertEval("[1 2] 3", "[1 2 3])");
    }

    @Test
    public void zip() throws ScriptExecutionException, InterruptedException {
        register("[", StackOperators.LEFT_SQUARE_BRACE);
        register("]", StackOperators.RIGHT_SQUARE_BRACE);
        register("zip", ArrayOperators.ZIP);
        assertEval("[[1 4 7] [2 5 8] [3 6 9]]", "[[1 2 3][4 5 6][7 8 9]]zip");
        assertEval("[[1 3 5 7] [2 4 6 8]]", "[[1 2][3 4][5 6][7 8]]zip");
        assertEval("[[1 2] [3 4] [5 6] [7 8]]", "[[1 3 5 7][2 4 6 8]]zip");
    }

    @Test
    public void fold() throws ScriptExecutionException, InterruptedException {
        register("[", StackOperators.LEFT_SQUARE_BRACE);
        register("]", StackOperators.RIGHT_SQUARE_BRACE);
        register("*", new OrderedDispatcher("*"));
        register("*_number_number", ArithmeticOperators.MULTIPLICATION);
        register("*_block_array", ArrayOperators.FOLD);
        assertEval("6", "1 2 3]{*}*");
    }

    @Test
    public void foldOne() throws ScriptExecutionException, InterruptedException {
        register("[", StackOperators.LEFT_SQUARE_BRACE);
        register("]", StackOperators.RIGHT_SQUARE_BRACE);
        register("*", new OrderedDispatcher("*"));
        register("*_number_number", ArithmeticOperators.MULTIPLICATION);
        register("*_block_array", ArrayOperators.FOLD);
        assertEval("6", "6]{*}*");
    }

    @Test
    public void sort() throws ScriptExecutionException, InterruptedException {
        register("[", StackOperators.LEFT_SQUARE_BRACE);
        register("]", StackOperators.RIGHT_SQUARE_BRACE);
        register("$", ArrayOperators.SORT);
        assertEval("[1 3 4 5 6]", "4 6 1 3 5]$");
    }

    @Test
    public void sortByMapping() throws ScriptExecutionException, InterruptedException {
        register("[", StackOperators.LEFT_SQUARE_BRACE);
        register("]", StackOperators.RIGHT_SQUARE_BRACE);
        register("*", ArithmeticOperators.MULTIPLICATION);
        register("$", ArrayOperators.SORT_BY_MAPPING);
        assertEval("[5 4 3 2 1]", "[5 4 3 1 2]{-1*}$");
    }

    @Test
    public void concatenate() throws ScriptExecutionException, InterruptedException {
        register("[", StackOperators.LEFT_SQUARE_BRACE);
        register("]", StackOperators.RIGHT_SQUARE_BRACE);
        register("+", ArrayOperators.CONCATENATE);
        assertEval("[1 2 3 4 5]", "[1 2 3][4 5]+");
    }

    @Test
    public void substract() throws ScriptExecutionException, InterruptedException {
        register("[", StackOperators.LEFT_SQUARE_BRACE);
        register("]", StackOperators.RIGHT_SQUARE_BRACE);
        register("-", ArrayOperators.SUBSTRACT);
        assertEval("[5 5 4]", "[5 2 5 4 1 1][1 2]-");
    }

    @Test
    public void repeat() throws ScriptExecutionException, InterruptedException {
        register("[", StackOperators.LEFT_SQUARE_BRACE);
        register("]", StackOperators.RIGHT_SQUARE_BRACE);
        register("*", new OrderedDispatcher("*"));
        register("*_array_number", ArrayOperators.REPEAT);
        assertEval("[1 2 3 1 2 3]", "[1 2 3]2*");
    }

    @Test
    public void join() throws ScriptExecutionException, InterruptedException {
        register("[", StackOperators.LEFT_SQUARE_BRACE);
        register("]", StackOperators.RIGHT_SQUARE_BRACE);
        register("*", ArrayOperators.JOIN_ARRAYS);
        assertEval("[1 4 2 4 3]", "[1 2 3][4]*");
    }

    @Test
    public void joinString() throws ScriptExecutionException, InterruptedException {
        register("[", StackOperators.LEFT_SQUARE_BRACE);
        register("]", StackOperators.RIGHT_SQUARE_BRACE);
        register("*", ArrayOperators.JOIN);
        assertEval("1,2,3", "[1 2 3]','*");
    }

    @Test
    public void splitAroundMatches() throws ScriptExecutionException, InterruptedException {
        register("[", StackOperators.LEFT_SQUARE_BRACE);
        register("]", StackOperators.RIGHT_SQUARE_BRACE);
        register("/", ArrayOperators.SPLIT_AROUND_MATCHES);
        assertEval("[[1] [4] [5]]", "[1 2 3 4 2 3 5][2 3]/");
    }

    @Test
    public void splitIntoGroups() throws ScriptExecutionException, InterruptedException {
        register("[", StackOperators.LEFT_SQUARE_BRACE);
        register("]", StackOperators.RIGHT_SQUARE_BRACE);
        register("/", new OrderedDispatcher("/"));
        register("/_array_number", ArrayOperators.SPLIT_INTO_GROUPS);
        assertEval("[[1 2] [3 4] [5]]", "[1 2 3 4 5] 2/");
    }

    @Test
    public void each() throws ScriptExecutionException, InterruptedException {
        register("[", StackOperators.LEFT_SQUARE_BRACE);
        register("]", StackOperators.RIGHT_SQUARE_BRACE);
        register("+", ArithmeticOperators.ADDITION);
        register("/", new OrderedDispatcher("/"));
        register("/_block_array", ArrayOperators.EACH);
        assertEval("2 3 4", "[1 2 3]{1+}/");
    }

}
