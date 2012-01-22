
package biz.metacode.calcscript.interpreter.execution.test;

import biz.metacode.calcscript.interpreter.ScriptExecutionException;
import biz.metacode.calcscript.interpreter.builtins.ArrayOperators;
import biz.metacode.calcscript.interpreter.builtins.Builtins;
import biz.metacode.calcscript.interpreter.builtins.MathOperators;
import biz.metacode.calcscript.interpreter.builtins.OrderedDispatcher;
import biz.metacode.calcscript.interpreter.builtins.StackOperators;
import biz.metacode.calcscript.interpreter.execution.EngineTestBase;
import biz.metacode.calcscript.interpreter.execution.RestoreException;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PoolTest extends EngineTestBase {

    @Test
    public void rot3Test() throws ScriptExecutionException, InterruptedException {
        register("@", StackOperators.ROT3);
        assertEval("2 3 1", "1 2 3@");
        // three numbers from above
        assertCurrentNumericPoolSize(3);
        // one SharedArray returned from engine.execute()
        assertCurrentArrayPoolSize(1);
        assertCurrentStringPoolSize(0);
    }

    @Test
    public void sumTest() throws ScriptExecutionException, InterruptedException {
        register("[", StackOperators.MARK_STACK_SIZE);
        register("]", StackOperators.SLICE_STACK);
        register("sum", MathOperators.SUM);
        assertEval("6", "[1 2 3]sum");
        // three numbers from above
        assertCurrentNumericPoolSize(3);
        // one SharedArray returned from engine.execute()
        // note that the array used for sum has been reused
        assertCurrentArrayPoolSize(1);
        assertCurrentStringPoolSize(0);
    }

    @Test
    public void mapTest() throws ScriptExecutionException, InterruptedException {
        register("[", StackOperators.MARK_STACK_SIZE);
        register("]", StackOperators.SLICE_STACK);
        register("%", new OrderedDispatcher("%"));
        register("%_block_array", ArrayOperators.MAP);
        register(",", ArrayOperators.GET_LENGTH);
        assertEval("[1 3 2]", "[[1][4 1 6][3 5]]{,}%");
        // numbers from arrays plus array lengths
        assertCurrentNumericPoolSize(9);
        // three inner arrays plus outer one
        // note that the array used for sum has been reused
        assertCurrentArrayPoolSize(4);
        assertCurrentStringPoolSize(0);
    }

    @Test
    public void memoryTest() throws ScriptExecutionException, InterruptedException {
        register(Builtins.getBuiltins());
        assertEvalWithoutBalance("6", "1 2][3 4]]:b;{~.@-.*\\/}:c;1 2 3]sum");
        // numbers assigned to "b" are int memory thus not released
        assertCurrentNumericPoolSize(3);
        // arrays assigned to "b" are not released to pool only the array used
        // for sum and engine.execute is released
        assertCurrentArrayPoolSize(1);
        assertCurrentStringPoolSize(0);
    }

    @Test
    public void memoryOverwriteTest() throws ScriptExecutionException, InterruptedException {
        register("[", StackOperators.MARK_STACK_SIZE);
        register("]", StackOperators.SLICE_STACK);
        register(";", StackOperators.DROP);
        assertEvalWithoutBalance("6", "1 2][3 4]]:b;6");

        assertCurrentNumericPoolSize(1);
        assertCurrentArrayPoolSize(1);
        assertCurrentStringPoolSize(0);

        assertEvalWithoutBalance("7", "6:b;7");

        // four numbers from nested array "b" has been released
        assertCurrentNumericPoolSize(4);
        // three arrays consisting of "b" plus array used by stack
        assertCurrentArrayPoolSize(4);
        assertCurrentStringPoolSize(0);
    }

    @Test
    public void serializationTest() throws ScriptExecutionException, IOException, RestoreException,
            InterruptedException {
        register("[", StackOperators.MARK_STACK_SIZE);
        register("]", StackOperators.SLICE_STACK);
        register(";", StackOperators.DROP);
        assertEvalWithoutBalance("6", "1 2]:b;6");
        // numbers assigned to "b" are int memory thus not released
        assertCurrentNumericPoolSize(1);
        // arrays assigned to "b" are not released to pool only the array used
        // for sum and engine.execute is released
        assertCurrentArrayPoolSize(1);
        assertCurrentStringPoolSize(0);

        // serializing memory to stream
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        saveState(outStream);

        ByteArrayInputStream inStream = new ByteArrayInputStream(outStream.toByteArray());
        newEngine();
        assertEval("", "b");

        restoreState(inStream);
        assertEval("[1 2]", "b");
        // numbers are not released because they exist in memory
        assertCurrentNumericPoolSize(0);
        // arrays assigned to "b" are not released to pool only the array used
        // for sum and engine.execute is released
        assertCurrentArrayPoolSize(1);
        assertCurrentStringPoolSize(0);
    }

    @Test
    public void duplicateTest() throws ScriptExecutionException, InterruptedException {
        register("[", StackOperators.MARK_STACK_SIZE);
        register("]", StackOperators.SLICE_STACK);
        register("sum", MathOperators.SUM);
        register("\\", StackOperators.SWAP);
        register(".", StackOperators.DUPLICATE);
        assertEval("6", "1 2].sum\\sum]sum");
    }

    @Test
    public void clearingSerializableTest() throws ScriptExecutionException, IOException,
            RestoreException, InterruptedException {
        register(Builtins.getBuiltins());
        assertEvalWithoutBalance("6", "1 2][3 4]]:b;6");

        // serializing memory to stream
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        saveState(outStream);

        ByteArrayInputStream inStream = new ByteArrayInputStream(outStream.toByteArray());
        newEngine();

        restoreState(inStream);
        assertEval("10", "b{~+}%sum");
        // intermediate sums are released
        assertCurrentNumericPoolSize(2);
        // arrays assigned to "b" are not released to pool only the array used
        // for sum and engine.execute is released
        assertCurrentArrayPoolSize(1);
        assertCurrentStringPoolSize(0);

        assertEvalWithoutBalance("6", "7:b;6");
        // one numeric is held in memory, the others are released with the array
        // "b"
        assertCurrentNumericPoolSize(5);
        // nested arrays assigned to "b" are released
        assertCurrentArrayPoolSize(4);
        assertCurrentStringPoolSize(0);
    }
}
