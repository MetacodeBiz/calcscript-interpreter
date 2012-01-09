
package biz.metacode.clients.calcscript.interpreter;

import biz.metacode.clients.calcscript.interpreter.builtins.StandardOperators;
import biz.metacode.clients.calcscript.interpreter.execution.Engine;
import biz.metacode.clients.calcscript.interpreter.execution.ExecutionException;

import java.util.List;

public class Main {

    /**
     * @param args
     * @throws ExecutionException
     */
    public static void main(String[] args) throws ExecutionException {
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
        List<Object> output = engine.execute("{4+}:a;5 a");
        for (Object out : output) {
            System.out.println("==> " + out);
        }
    }

}
