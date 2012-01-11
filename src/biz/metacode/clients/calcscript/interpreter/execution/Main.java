
package biz.metacode.clients.calcscript.interpreter.execution;

import biz.metacode.clients.calcscript.interpreter.Value;
import biz.metacode.clients.calcscript.interpreter.builtins.ArithmeticOperators;
import biz.metacode.clients.calcscript.interpreter.builtins.MathOperators;
import biz.metacode.clients.calcscript.interpreter.builtins.StackOperators;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class Main {

    /**
     * @param args
     * @throws ExecutionException
     * @throws IOException
     */
    public static void main(String[] args) throws Exception {
/*
        Lexer lex = new Lexer("{4+}:a;");
        for(String token : lex) {
        System.out.println("TOK: " + token);
        }*/
        Engine engine = new Engine();
        engine.register("+", ArithmeticOperators.ADDITION);
        engine.register("[", StackOperators.LEFT_SQUARE_BRACE);
        engine.register("]", StackOperators.RIGHT_SQUARE_BRACE);
        engine.register("sum", MathOperators.SUM);
        List<Value> output = engine.execute("[1 2 3]:b");
        for (Value out : output) {
            System.out.println("==> " + out);
        }

        ByteArrayOutputStream str = new ByteArrayOutputStream();
        engine.saveState(str);

        ByteArrayInputStream str2 = new ByteArrayInputStream(str.toByteArray());
        engine = new Engine();
        System.out.println("BEFORE");
        List<Value> output2 = engine.execute("b");
        for (Value out : output2) {
            System.out.println("==> " + out);
        }

        engine.restoreState(str2);
        System.out.println("AFTER");
        List<Value> output3 = engine.execute("b");
        for (Value out : output3) {
            System.out.println("==> " + out);
        }
    }

}
