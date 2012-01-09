
package biz.metacode.clients.calcscript.interpreter;

import biz.metacode.clients.calcscript.interpreter.builtins.StandardOperators;
import biz.metacode.clients.calcscript.interpreter.execution.Engine;
import biz.metacode.clients.calcscript.interpreter.execution.ExecutionException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class Main {

    /**
     * @param args
     * @throws ExecutionException
     * @throws IOException
     */
    public static void main(String[] args) throws ExecutionException, IOException {
/*
        Lexer lex = new Lexer("{4+}:a;");
        for(String token : lex) {
        System.out.println("TOK: " + token);
        }*/
        Engine engine = new Engine();
        engine.register("+", StandardOperators.ADDITION);
        engine.register("[", StandardOperators.LEFT_SQUARE_BRACE);
        engine.register("]", StandardOperators.RIGHT_SQUARE_BRACE);
        engine.register("sum", StandardOperators.SUM);
        List<Serializable> output = engine.execute("{4+}:a;5 a:b");
        for (Serializable out : output) {
            System.out.println("==> " + out);
        }

        ByteArrayOutputStream str = new ByteArrayOutputStream();
        engine.saveState(str);

        ByteArrayInputStream str2 = new ByteArrayInputStream(str.toByteArray());
        engine = new Engine();
        System.out.println("BEFORE");
        List<Serializable> output2 = engine.execute("b");
        for (Serializable out : output2) {
            System.out.println("==> " + out);
        }

        engine.restoreState(str2);
        System.out.println("AFTER");
        List<Serializable> output3 = engine.execute("b");
        for (Serializable out : output3) {
            System.out.println("==> " + out);
        }
    }

}
