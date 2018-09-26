package biz.metacode.calcscript.interpreter.builtins.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import biz.metacode.calcscript.interpreter.Invocable;
import biz.metacode.calcscript.interpreter.ScriptExecutionException;
import biz.metacode.calcscript.interpreter.builtins.Builtins;
import biz.metacode.calcscript.interpreter.builtins.Operators;
import biz.metacode.calcscript.interpreter.builtins.OrderedDispatcher;
import biz.metacode.calcscript.interpreter.builtins.SingleDispatcher;
import biz.metacode.calcscript.interpreter.execution.Engine;
import biz.metacode.calcscript.interpreter.source.Program;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class OperatorsTest {

    @Test(expected = ScriptExecutionException.class)
    public void orderedDispatcher_noOverload() throws InterruptedException {
        executeDispatcher(new OrderedDispatcher("x"));
    }

    @Test(expected = ScriptExecutionException.class)
    public void singleDispatcher_noOverload() throws InterruptedException {
        executeDispatcher(new SingleDispatcher("x"));
    }

    @Test
    public void constructors() {
        assertConstructorIsPrivate(Operators.class);
        assertConstructorIsPrivate(Builtins.class);
    }

    private void executeDispatcher(Invocable dispatcher) throws InterruptedException {
        Map<String, Invocable> operators = new HashMap<>();
        operators.put("x", dispatcher);
        Engine engine = new Engine();
        engine.register(operators);
        engine.execute(new Program("1 2 x"));
    }

    private void assertConstructorIsPrivate(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            assertTrue(Modifier.isPrivate(constructor.getModifiers()));
            constructor.setAccessible(true);
            constructor.newInstance();
        } catch (ReflectiveOperationException | IllegalArgumentException | SecurityException e) {
            fail("Should not throw: " + e);
        }
    }
}
