
package biz.metacode.clients.calcscript.interpreter.builtins;

import org.junit.Before;

import junit.framework.Assert;
import biz.metacode.clients.calcscript.interpreter.Invocable;
import biz.metacode.clients.calcscript.interpreter.SharedArray;
import biz.metacode.clients.calcscript.interpreter.Value;
import biz.metacode.clients.calcscript.interpreter.execution.Engine;
import biz.metacode.clients.calcscript.interpreter.execution.ScriptExecutionException;

public abstract class OperatorTestBase {

    private Engine engine;

    @Before
    public void setupEngine() {
        engine = new Engine();
    }

    protected void register(String name, Invocable function) {
        engine.register(name, function);
    }

    protected String eval(String code) throws ScriptExecutionException, InterruptedException {
        SharedArray array = engine.execute(code);
        StringBuilder sb = new StringBuilder();
        try {
            for (Value out : array) {
                sb.append(out).append(' ');
            }
            if (sb.length() > 0) {
                sb.delete(sb.length() - 1, sb.length());
            }
            return sb.toString();
        } finally {
            array.release();
        }
    }

    protected void assertEval(String result, String code) throws ScriptExecutionException, InterruptedException {
        Assert.assertEquals(result, eval(code));
    }
}
