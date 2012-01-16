
package biz.metacode.clients.calcscript.interpreter.execution;

import biz.metacode.clients.calcscript.interpreter.SharedArray;
import biz.metacode.clients.calcscript.interpreter.Value;
import biz.metacode.clients.calcscript.interpreter.builtins.ArithmeticOperators;
import biz.metacode.clients.calcscript.interpreter.builtins.ArrayOperators;
import biz.metacode.clients.calcscript.interpreter.builtins.MathOperators;
import biz.metacode.clients.calcscript.interpreter.builtins.StackOperators;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PoolTest {
    private static String toString(SharedArray array) {
        StringBuilder sb = new StringBuilder();
        try {
            for (Value out : array) {
                sb.append(out).append(' ');
            }
            if (sb.length() > 0) {
                sb.delete(sb.length() - 1, sb.length());
            }
        } finally {
            array.release();
        }
        return sb.toString();
    }

    private static Engine getEngine() {
        Engine engine = new Engine();
        engine.register("+", ArithmeticOperators.ADDITION);
        engine.register("-", ArithmeticOperators.SUBSTRACTION);
        engine.register("*", ArithmeticOperators.MULTIPLICATION);
        engine.register("/", ArithmeticOperators.DIVISION);
        engine.register("[", StackOperators.LEFT_SQUARE_BRACE);
        engine.register("]", StackOperators.RIGHT_SQUARE_BRACE);
        engine.register("sum", MathOperators.SUM);
        engine.register("%", ArrayOperators.MAP);
        engine.register("abs", MathOperators.ABSOLUTE);
        engine.register(";", StackOperators.DROP);
        engine.register("\\", StackOperators.SWAP);
        engine.register(",", ArrayOperators.COMMA);
        engine.register(".", StackOperators.DUPLICATE);
        engine.register("~", ArrayOperators.EXTRACT);
        engine.register("@", StackOperators.ROT3);
        return engine;
    }

    @Test
    public void rot3Test() throws ExecutionException {
        Engine engine = getEngine();
        Assert.assertEquals("2 3 1", toString(engine.execute("1 2 3@")));
        // three numbers from above
        Assert.assertEquals(3, engine.getTestHelper().getCurrentNumericPoolSize());
        // one SharedArray returned from engine.execute()
        Assert.assertEquals(1, engine.getTestHelper().getCurrentArrayPoolSize());
        Assert.assertEquals(0, engine.getTestHelper().getCurrentTextPoolSize());
    }

    @Test
    public void sumTest() throws ExecutionException {
        Engine engine = getEngine();
        Assert.assertEquals("6", toString(engine.execute("[1 2 3]sum")));
        // three numbers from above
        Assert.assertEquals(3, engine.getTestHelper().getCurrentNumericPoolSize());
        // one SharedArray returned from engine.execute()
        // note that the array used for sum has been reused
        Assert.assertEquals(1, engine.getTestHelper().getCurrentArrayPoolSize());
        Assert.assertEquals(0, engine.getTestHelper().getCurrentTextPoolSize());
    }

    @Test
    public void mapTest() throws ExecutionException {
        Engine engine = getEngine();
        Assert.assertEquals("[1 3 2]", toString(engine.execute("[[1][4 1 6][3 5]]{,}%")));
        // numbers from arrays plus array lengths
        Assert.assertEquals(9, engine.getTestHelper().getCurrentNumericPoolSize());
        // three inner arrays plus outer one
        // note that the array used for sum has been reused
        Assert.assertEquals(4, engine.getTestHelper().getCurrentArrayPoolSize());
        Assert.assertEquals(0, engine.getTestHelper().getCurrentTextPoolSize());
    }

    @Test
    public void memoryTest() throws ExecutionException {
        Engine engine = getEngine();
        Assert.assertEquals("6", toString(engine.execute("1 2][3 4]]:b;{~.@-.*\\/}:c;1 2 3]sum")));
        // numbers assigned to "b" are int memory thus not released
        Assert.assertEquals(3, engine.getTestHelper().getCurrentNumericPoolSize());
        // arrays assigned to "b" are not released to pool only the array used
        // for sum and engine.execute is released
        Assert.assertEquals(1, engine.getTestHelper().getCurrentArrayPoolSize());
        Assert.assertEquals(0, engine.getTestHelper().getCurrentTextPoolSize());
    }

    @Test
    public void memoryOverwriteTest() throws ExecutionException {
        Engine engine = getEngine();
        Assert.assertEquals("6", toString(engine.execute("1 2][3 4]]:b;6")));

        Assert.assertEquals(1, engine.getTestHelper().getCurrentNumericPoolSize());
        Assert.assertEquals(1, engine.getTestHelper().getCurrentArrayPoolSize());
        Assert.assertEquals(0, engine.getTestHelper().getCurrentTextPoolSize());

        Assert.assertEquals("7", toString(engine.execute("6:b;7")));

        // four numbers from nested array "b" has been released
        Assert.assertEquals(4, engine.getTestHelper().getCurrentNumericPoolSize());
        // three arrays consisting of "b" plus array used by stack
        Assert.assertEquals(4, engine.getTestHelper().getCurrentArrayPoolSize());
        Assert.assertEquals(0, engine.getTestHelper().getCurrentTextPoolSize());
    }

    @Test
    public void serializationTest() throws ExecutionException, IOException, RestoreException {
        Engine engine = getEngine();
        Assert.assertEquals("6", toString(engine.execute("1 2]:b;6")));
        // numbers assigned to "b" are int memory thus not released
        Assert.assertEquals(1, engine.getTestHelper().getCurrentNumericPoolSize());
        // arrays assigned to "b" are not released to pool only the array used
        // for sum and engine.execute is released
        Assert.assertEquals(1, engine.getTestHelper().getCurrentArrayPoolSize());
        Assert.assertEquals(0, engine.getTestHelper().getCurrentTextPoolSize());

        // serializing memory to stream
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        engine.saveState(outStream);

        ByteArrayInputStream inStream = new ByteArrayInputStream(outStream.toByteArray());
        engine = getEngine();
        Assert.assertEquals("", toString(engine.execute("b")));

        engine.restoreState(inStream);
        Assert.assertEquals("[1 2]", toString(engine.execute("b")));
        // numbers are not released because they exist in memory
        Assert.assertEquals(0, engine.getTestHelper().getCurrentNumericPoolSize());
        // arrays assigned to "b" are not released to pool only the array used
        // for sum and engine.execute is released
        Assert.assertEquals(1, engine.getTestHelper().getCurrentArrayPoolSize());
        Assert.assertEquals(0, engine.getTestHelper().getCurrentTextPoolSize());
    }
}
