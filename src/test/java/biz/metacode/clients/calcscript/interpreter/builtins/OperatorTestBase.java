
package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.Invocable;
import biz.metacode.clients.calcscript.interpreter.SharedArray;
import biz.metacode.clients.calcscript.interpreter.Value;
import biz.metacode.clients.calcscript.interpreter.execution.Engine;
import biz.metacode.clients.calcscript.interpreter.execution.ScriptExecutionException;

import org.junit.Rule;
import org.junit.rules.TestName;

import java.util.Random;

import junit.framework.Assert;

public abstract class OperatorTestBase {

    private static Engine engine = new Engine();

    @Rule
    public TestName name = new TestName();

    protected void register(String name, Invocable function) {
        engine.register(name, function);
    }

    protected String eval(String code) throws ScriptExecutionException, InterruptedException {
        engine.getTestHelper().setTrait("test:" + name.getMethodName() + (new Random().nextInt()));
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
