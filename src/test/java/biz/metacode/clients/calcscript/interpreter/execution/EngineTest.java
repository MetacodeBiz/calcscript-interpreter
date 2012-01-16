
package biz.metacode.clients.calcscript.interpreter.execution;

import biz.metacode.clients.calcscript.interpreter.SharedArray;
import biz.metacode.clients.calcscript.interpreter.Value;
import biz.metacode.clients.calcscript.interpreter.builtins.ArithmeticOperators;
import biz.metacode.clients.calcscript.interpreter.builtins.ArrayOperators;
import biz.metacode.clients.calcscript.interpreter.builtins.MathOperators;
import biz.metacode.clients.calcscript.interpreter.builtins.StackOperators;

import org.junit.Assert;
import org.junit.Test;

public class EngineTest {
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
    }

    @Test
    public void sumTest() throws ExecutionException {
        Engine engine = getEngine();
        Assert.assertEquals("6", toString(engine.execute("[1 2 3]sum")));
    }
}
