
package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.execution.ScriptExecutionException;

import org.junit.Test;

public class BitOperatorsTest extends OperatorTestBase {

    @Test
    public void bitwiseOr() throws ScriptExecutionException, InterruptedException {
        register("|", new OrderedDispatcher("|"));
        register("|_number_number", BitOperators.OR);
        assertEval("7", "5 3 |");
    }

    @Test
    public void bitwiseAnd() throws ScriptExecutionException, InterruptedException {
        register("&", new OrderedDispatcher("&"));
        register("&_number_number", BitOperators.AND);
        assertEval("1", "5 3 &");
    }

    @Test
    public void bitwiseXor() throws ScriptExecutionException, InterruptedException {
        register("^", new OrderedDispatcher("^"));
        register("^_number_number", BitOperators.XOR);
        assertEval("6", "5 3 ^");
    }
}
