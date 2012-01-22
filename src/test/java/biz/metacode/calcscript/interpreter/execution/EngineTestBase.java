
package biz.metacode.calcscript.interpreter.execution;

import biz.metacode.calcscript.interpreter.Invocable;
import biz.metacode.calcscript.interpreter.SharedArray;
import biz.metacode.calcscript.interpreter.Value;
import biz.metacode.calcscript.interpreter.execution.Engine;
import biz.metacode.calcscript.interpreter.execution.RestoreException;
import biz.metacode.calcscript.interpreter.execution.ScriptExecutionException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import junit.framework.Assert;

public abstract class EngineTestBase {

    private Engine engine;

    @Before
    public void createEngine() {
        engine = new Engine();
    }

    @Rule
    public TestName name = new TestName();

    protected void register(String name, Invocable function) {
        engine.register(name, function);
    }

    protected void register(Map<String, Invocable> functions) {
        engine.register(functions);
    }

    protected void newEngine() {
        engine = new Engine();
    }

    protected void saveState(OutputStream outStr) throws IOException {
        engine.saveState(outStr);
    }

    protected void restoreState(InputStream inStr) throws RestoreException {
        engine.restoreState(inStr);
    }

    protected void assertCurrentNumericPoolSize(int size) {
        Assert.assertEquals(size, engine.getTestHelper().getCurrentNumericPoolSize());
    }

    protected void assertCurrentArrayPoolSize(int size) {
        Assert.assertEquals(size, engine.getTestHelper().getCurrentArrayPoolSize());
    }

    protected void assertCurrentStringPoolSize(int size) {
        Assert.assertEquals(size, engine.getTestHelper().getCurrentTextPoolSize());
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

    protected void assertEvalWithoutBalance(String result, String code)
            throws ScriptExecutionException, InterruptedException {
        Assert.assertEquals(result, eval(code));
    }

    protected void assertEval(String result, String code) throws ScriptExecutionException,
            InterruptedException {
        engine.getTestHelper().resetAllocationBalance();
        assertEvalWithoutBalance(result, code);
        Assert.assertEquals("Numeric balance should be 0", 0, engine.getTestHelper()
                .getNumericAllocationBalance());
        Assert.assertEquals("Array balance should be 0", 0, engine.getTestHelper()
                .getArrayAllocationBalance());
        Assert.assertEquals("String balance should be 0", 0, engine.getTestHelper()
                .getTextAllocationBalance());
    }
}
