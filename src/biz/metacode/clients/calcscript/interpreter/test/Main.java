
package biz.metacode.clients.calcscript.interpreter.test;

import biz.metacode.clients.calcscript.interpreter.SharedArray;
import biz.metacode.clients.calcscript.interpreter.Value;
import biz.metacode.clients.calcscript.interpreter.builtins.ArithmeticOperators;
import biz.metacode.clients.calcscript.interpreter.builtins.ArrayOperators;
import biz.metacode.clients.calcscript.interpreter.builtins.MathOperators;
import biz.metacode.clients.calcscript.interpreter.builtins.StackOperators;
import biz.metacode.clients.calcscript.interpreter.execution.Engine;
import biz.metacode.clients.calcscript.interpreter.execution.ExecutionException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
     * @throws ExecutionException
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
        print(engine.execute("1 2 3@"));
        print(engine.execute("1 2 3@"));
        print(engine.execute("1 2][3 4]]:b;{~.@-.*\\/}:c;1 2 3]sum"));

        ByteArrayOutputStream str = new ByteArrayOutputStream();
        engine.saveState(str);

        ByteArrayInputStream str2 = new ByteArrayInputStream(str.toByteArray());
        engine = new Engine();
        engine.register(".", StackOperators.DUPLICATE);
        System.out.println("BEFORE");
        print(engine.execute("b"));

        engine.restoreState(str2);
        System.out.println("AFTER");
        print(engine.execute("b{c}"));
    }

}
