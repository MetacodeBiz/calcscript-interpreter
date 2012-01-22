package biz.metacode.calcscript.interpreter.builtins.test;

import biz.metacode.calcscript.interpreter.ScriptExecutionException;
import biz.metacode.calcscript.interpreter.builtins.ArithmeticOperators;
import biz.metacode.calcscript.interpreter.builtins.BlockOperators;
import biz.metacode.calcscript.interpreter.builtins.CoercingDispatcher;
import biz.metacode.calcscript.interpreter.builtins.ComparisonOperators;
import biz.metacode.calcscript.interpreter.builtins.OrderedDispatcher;
import biz.metacode.calcscript.interpreter.builtins.SingleDispatcher;
import biz.metacode.calcscript.interpreter.builtins.StackOperators;
import biz.metacode.calcscript.interpreter.execution.EngineTestBase;

import org.junit.Test;

public class BlockOperatorsTest extends EngineTestBase {

    @Test
    public void concatenate() throws ScriptExecutionException, InterruptedException {
        register("+", new CoercingDispatcher("+"));
        register("+_block_block", BlockOperators.CONCATENATE);
        // FIXME: Space should be preserved in output
        assertEval("{asdf1234}", "'asdf'{1234}+");
    }

    @Test
    public void unfold() throws ScriptExecutionException, InterruptedException {
        register("<", ComparisonOperators.LESS_THAN);
        register(".", StackOperators.DUPLICATE);
        register("@", StackOperators.ROT3);
        register("+", ArithmeticOperators.ADDITION);
        register("/", BlockOperators.UNFOLD);
        assertEval("89 [1 1 2 3 5 8 13 21 34 55 89]", "0 1 {100<} { .@+ } /");
    }

    @Test
    public void filter() throws ScriptExecutionException, InterruptedException {
        register("[", StackOperators.MARK_STACK_SIZE);
        register("]", StackOperators.SLICE_STACK);
        register("%", ArithmeticOperators.MODULO);
        register(",", new SingleDispatcher(","));
        register(",_block", BlockOperators.FILTER);
        assertEval("[1 2 4 5 7 8]", "[0 1 2 3 4 5 6 7 8 9]{3%},");
    }

    @Test
    public void find() throws ScriptExecutionException, InterruptedException {
        register("[", StackOperators.MARK_STACK_SIZE);
        register("]", StackOperators.SLICE_STACK);
        register(".", StackOperators.DUPLICATE);
        register("*", ArithmeticOperators.MULTIPLICATION);
        register(">", ComparisonOperators.GREATER_THAN);
        register("?", new OrderedDispatcher("?"));
        register("?_block_array", BlockOperators.FIND);
        assertEval("5", "[1 2 3 4 5 6] {.* 20>} ?");
        assertEval("", "[] {.* 20>} ?");
    }

    @Test
    public void execute() throws ScriptExecutionException, InterruptedException {
        register("+", ArithmeticOperators.ADDITION);
        register("~", new SingleDispatcher("~"));
        register("~_block", BlockOperators.EXECUTE);
        assertEval("3", "{1 2+}~");
    }

    @Test
    public void getNth() throws ScriptExecutionException, InterruptedException {
        register("$", StackOperators.GET_NTH);
        assertEval("1 2 3 4 5 4", "1 2 3 4 5  1$");
    }
}
