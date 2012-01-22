
package biz.metacode.clients.calcscript.interpreter.execution;

import biz.metacode.clients.calcscript.interpreter.Invocable;
import biz.metacode.clients.calcscript.interpreter.SharedArray;
import biz.metacode.clients.calcscript.interpreter.Value;
import biz.metacode.clients.calcscript.interpreter.execution.Engine;
import biz.metacode.clients.calcscript.interpreter.execution.ScriptExecutionException;

import org.junit.Rule;
import org.junit.rules.TestName;

import junit.framework.Assert;

public abstract class EngineTestBase {

    private static Engine engine = new Engine();

    @Rule
    public TestName name = new TestName();

    protected void register(String name, Invocable function) {
        engine.register(name, function);
    }

    protected String eval(String code) throws ScriptExecutionException, InterruptedException {
        engine.getTestHelper().setTrait(name.getMethodName());
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

    protected void assertEval(String result, String code) throws ScriptExecutionException,
            InterruptedException {
        engine.getTestHelper().resetAllocationBalance();
        Assert.assertEquals(result, eval(code));
        Assert.assertEquals("Numeric balance should be 0", 0, engine.getTestHelper()
                .getNumericAllocationBalance());
        Assert.assertEquals("Array balance should be 0", 0, engine.getTestHelper()
                .getArrayAllocationBalance());
        Assert.assertEquals("String balance should be 0", 0, engine.getTestHelper()
                .getTextAllocationBalance());
    }
}
