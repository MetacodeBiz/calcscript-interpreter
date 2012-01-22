package biz.metacode.calcscript.interpreter.builtins.test;

import biz.metacode.calcscript.interpreter.ScriptExecutionException;
import biz.metacode.calcscript.interpreter.builtins.ArithmeticOperators;
import biz.metacode.calcscript.interpreter.builtins.OrderedDispatcher;
import biz.metacode.calcscript.interpreter.builtins.SingleDispatcher;
import biz.metacode.calcscript.interpreter.builtins.StringOperators;
import biz.metacode.calcscript.interpreter.execution.EngineTestBase;

import org.junit.Test;

public class StringOperatorsTest extends EngineTestBase {

    @Test
    public void splitAroundMatchesNonEmpty() throws ScriptExecutionException, InterruptedException {
        register("%", new OrderedDispatcher("%"));
        register("%_string_string", StringOperators.SPLIT_AROUND_MATCHES_NONEMPTY);
        assertEval("[a df]", "'assdfs' 's'%");
    }

    @Test
    public void eval() throws ScriptExecutionException, InterruptedException {
        register("+", ArithmeticOperators.ADDITION);
        register("~", new SingleDispatcher("~"));
        register("~_string", StringOperators.EVAL);
        assertEval("3", "'1 2+'~");
    }

    @Test
    public void concatenate() throws ScriptExecutionException, InterruptedException {
        register("+", StringOperators.CONCATENATE);
        assertEval("asdf", "'as''df'+");
    }

    @Test
    public void objectToString() throws ScriptExecutionException, InterruptedException {
        register("`", StringOperators.TO_STRING);
        assertEval("{asdf}", "{asdf}`");
    }

    @Test
    public void sortLetters() throws ScriptExecutionException, InterruptedException {
        register("$", StringOperators.SORT_LETTERS);
        assertEval("adfs", "'asdf'$");
    }

    @Test
    public void repeat() throws ScriptExecutionException, InterruptedException {
        register("*", StringOperators.REPEAT);
        assertEval("asdfasdfasdf", "3'asdf'*");
    }

    @Test
    public void join() throws ScriptExecutionException, InterruptedException {
        register("*", StringOperators.JOIN);
        assertEval("a s d f", "'asdf'' '*");
    }
}
