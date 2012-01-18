
package biz.metacode.clients.calcscript.interpreter.test;

import biz.metacode.clients.calcscript.interpreter.SharedArray;
import biz.metacode.clients.calcscript.interpreter.Value;
import biz.metacode.clients.calcscript.interpreter.builtins.ArithmeticOperators;
import biz.metacode.clients.calcscript.interpreter.builtins.ArrayOperators;
import biz.metacode.clients.calcscript.interpreter.builtins.LoopOperators;
import biz.metacode.clients.calcscript.interpreter.builtins.MathOperators;
import biz.metacode.clients.calcscript.interpreter.builtins.StackOperators;
import biz.metacode.clients.calcscript.interpreter.execution.Engine;
import biz.metacode.clients.calcscript.interpreter.execution.ScriptExecutionException;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Main {

    private static void print(SharedArray array) {
        System.out.println("Array: " + Integer.toHexString(System.identityHashCode(array)));
        try {
            for (Value out : array) {
                System.out.println("==> " + out);
            }
        } finally {
            array.release();
        }
    }

    /**
     * @param args
     * @throws ScriptExecutionException
     * @throws IOException
     */
    public static void main(String[] args) throws Exception {
        /*
         * Lexer lex = new Lexer("{4+}:a;"); for(String token : lex) {
         * System.out.println("TOK: " + token); }
         */
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
        engine.register("do", LoopOperators.DO);

        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<SharedArray> future = service.submit(engine.executeLater("5do"));

        try {
            SharedArray result = future.get(3, TimeUnit.SECONDS);
            print(result);
        } catch(TimeoutException e) {
            System.out.println("Timed out!");
            future.cancel(true);
            service.shutdown();
        }
    }

}
