package biz.metacode.calcscript.interpreter.execution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import biz.metacode.calcscript.interpreter.ExecutionContext;
import biz.metacode.calcscript.interpreter.InvalidTypeException;
import biz.metacode.calcscript.interpreter.Invocable;
import biz.metacode.calcscript.interpreter.Value;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import org.junit.Test;

public class EngineTest {

    @Test
    public void invalidType() throws InterruptedException {
        try {
            withContext(context -> {
                context.pushString("x");
                context.popDouble();
            });
            fail("Should throw an exception.");
        } catch (InvalidTypeException e) {
            assertEquals(Value.Type.STRING, e.getActualType());
            assertEquals(Value.Type.NUMBER, e.getExpectedType());
        }
    }

    @Test
    public void setMethods() throws InterruptedException {
        withContext(context -> {
            Value first = context.convertToValue("test");
            Value second = context.convertToValue(0);
            Value array = context.convertToValue(Arrays.asList(first, second));
            Value array2 = context.convertToValue(Arrays.asList(second, first));
            Set<Value> values = new HashSet<>();
            values.add(array);
            assertTrue(values.contains(array));
            assertTrue(array.equals(array));
            assertFalse(array.equals(new Object()));
            assertFalse(array.equals(first));
            assertFalse(array.equals(array2));
            assertEquals(0, array.compareTo(array));
        });
    }

    private void withContext(Consumer<ExecutionContext> consumer) throws InterruptedException {
        new Engine().execute(new Invocable() {
            private static final long serialVersionUID = -1528855734795162653L;

            @Override
            public void invoke(ExecutionContext context) throws InterruptedException {
                consumer.accept(context);
            }
        });
    }
}
