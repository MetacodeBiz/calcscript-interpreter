
package biz.metacode.clients.calcscript.interpreter.test;

import biz.metacode.clients.calcscript.interpreter.SharedArray;
import biz.metacode.clients.calcscript.interpreter.Value;
import biz.metacode.clients.calcscript.interpreter.builtins.ArithmeticOperators;
import biz.metacode.clients.calcscript.interpreter.builtins.ArrayOperators;
import biz.metacode.clients.calcscript.interpreter.builtins.BitOperators;
import biz.metacode.clients.calcscript.interpreter.builtins.BlockOperators;
import biz.metacode.clients.calcscript.interpreter.builtins.BooleanOperators;
import biz.metacode.clients.calcscript.interpreter.builtins.CoercingDispatcher;
import biz.metacode.clients.calcscript.interpreter.builtins.ComparisonOperators;
import biz.metacode.clients.calcscript.interpreter.builtins.LoopOperators;
import biz.metacode.clients.calcscript.interpreter.builtins.MathOperators;
import biz.metacode.clients.calcscript.interpreter.builtins.OrderedDispatcher;
import biz.metacode.clients.calcscript.interpreter.builtins.SingleDispatcher;
import biz.metacode.clients.calcscript.interpreter.builtins.StackOperators;
import biz.metacode.clients.calcscript.interpreter.builtins.StringOperators;
import biz.metacode.clients.calcscript.interpreter.execution.Engine;
import biz.metacode.clients.calcscript.interpreter.execution.ScriptExecutionException;

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
     * @throws ScriptExecutionException
     * @throws IOException
     */
    public static void main(String[] args) throws Exception {
        Engine engine = new Engine();

        engine.register("+", new CoercingDispatcher("+"));
        engine.register("+_number_number", ArithmeticOperators.ADDITION);
        engine.register("+_string_string", StringOperators.CONCATENATE);
        engine.register("+_array_array", ArrayOperators.CONCATENATE);
        engine.register("+_block_block", BlockOperators.CONCATENATE);

        engine.register("-", new CoercingDispatcher("-"));
        engine.register("-_number_number", ArithmeticOperators.SUBSTRACTION);
        engine.register("-_array_array", ArrayOperators.SUBSTRACT);

        engine.register("*", new OrderedDispatcher("*"));
        engine.register("*_number_number", ArithmeticOperators.MULTIPLICATION);
        engine.register("*_block_number", LoopOperators.TIMES);
        engine.register("*_array_number", ArrayOperators.REPEAT);
        engine.register("*_string_number", StringOperators.REPEAT);
        engine.register("*_string_array", ArrayOperators.JOIN);
        engine.register("*_array_array", ArrayOperators.JOIN_ARRAYS);
        engine.register("*_string_string", StringOperators.JOIN);
        engine.register("*_block_array", ArrayOperators.FOLD);

        engine.register("/", new OrderedDispatcher("/"));
        engine.register("/_number_number", ArithmeticOperators.DIVISION);
        engine.register("/_array_array", ArrayOperators.SPLIT_AROUND_MATCHES);
        //engine.register("/_string_string", StringOperators.SPLIT_AROUND_MATCHES);
        engine.register("/_array_number", ArrayOperators.SPLIT_INTO_GROUPS);
        engine.register("/_block_block", BlockOperators.UNFOLD);
        engine.register("/_block_array", ArrayOperators.EACH);

        engine.register("%", new OrderedDispatcher("%"));
        engine.register("%_number_number", ArithmeticOperators.MODULO);
        engine.register("%_block_array", ArrayOperators.MAP);
        engine.register("%_array_number", ArrayOperators.EVERY_NTH_ELEMENT);
        engine.register("%_string_string", StringOperators.SPLIT_AROUND_MATCHES_NONEMPTY);

        engine.register("$", new SingleDispatcher("$"));
        engine.register("$_number", StackOperators.GET_NTH);
        engine.register("$_array", ArrayOperators.SORT);
        engine.register("$_string", StringOperators.SORT_LETTERS);
        engine.register("$_block", ArrayOperators.SORT_BY_MAPPING);

        engine.register(",", new SingleDispatcher(","));
        engine.register(",_array", ArrayOperators.COMMA);
        engine.register(",_block", BlockOperators.FILTER);
        engine.register(",_number", ArrayOperators.CREATE_ARRAY);

        engine.register("~", new SingleDispatcher("~"));
        engine.register("~_number", ArithmeticOperators.BITWISE_NEGATION);
        engine.register("~_array", ArrayOperators.EXTRACT);

        engine.register("?", new OrderedDispatcher("?"));
        engine.register("?_array_number", ArrayOperators.INDEX_OF);
        engine.register("?_block_array", BlockOperators.FIND);
        engine.register("?_number_number", ArithmeticOperators.POWER);

        engine.register("|", new OrderedDispatcher("|"));
        engine.register("|_array_array", ArrayOperators.UNION);
        engine.register("|_number_number", BitOperators.OR);

        engine.register("&", new OrderedDispatcher("&"));
        engine.register("&_array_array", ArrayOperators.INTERSECTION);
        engine.register("&_number_number", BitOperators.AND);

        engine.register("^", new OrderedDispatcher("^"));
        engine.register("^_array_array", ArrayOperators.SYMMETRIC_DIFFERENCE);
        engine.register("^_number_number", BitOperators.XOR);

        engine.register("<", new OrderedDispatcher("<"));
        engine.register("<_array_number", ArrayOperators.INDEX_LESS_THAN);
        engine.register("<_number_number", ComparisonOperators.LESS_THAN);
        engine.register("<_string_string", ComparisonOperators.LESS_THAN);

        engine.register(">", new OrderedDispatcher(">"));
        engine.register(">_array_number", ArrayOperators.INDEX_GREATER_THAN);
        engine.register(">_number_number", ComparisonOperators.GREATER_THAN);
        engine.register(">_string_string", ComparisonOperators.GREATER_THAN);

        engine.register("=", new OrderedDispatcher("="));
        engine.register("=_number_number", ComparisonOperators.EQUALS);
        engine.register("=_string_string", ComparisonOperators.EQUALS);
        engine.register("=_array_number", ArrayOperators.GET_ELEMENT);

        engine.register("(", new SingleDispatcher("("));
        engine.register("(_array", ArrayOperators.UNCONS);
        engine.register("(_number", ArithmeticOperators.INCREMENT);

        engine.register(")", new SingleDispatcher(")"));
        engine.register(")_array", ArrayOperators.UNCONS_RIGHT);
        engine.register(")_number", ArithmeticOperators.DECREMENT);

        engine.register("`", StringOperators.TO_STRING);
        engine.register("!", BooleanOperators.NOT);

        engine.register(".", StackOperators.DUPLICATE);
        engine.register("@", StackOperators.ROT3);
        engine.register("\\", StackOperators.SWAP);
        engine.register(";", StackOperators.DROP);
        engine.register("[", StackOperators.LEFT_SQUARE_BRACE);
        engine.register("]", StackOperators.RIGHT_SQUARE_BRACE);

        engine.register("sum", MathOperators.SUM);
        engine.register("do", LoopOperators.DO);
        engine.register("abs", MathOperators.ABSOLUTE);

        engine.register("zip", ArrayOperators.ZIP);
        engine.register("or", BooleanOperators.OR);
        engine.register("and", BooleanOperators.AND);
        engine.register("xor", BooleanOperators.XOR);
        engine.register("if", BooleanOperators.IF);
        engine.register("while", LoopOperators.WHILE);
        engine.register("until", LoopOperators.UNTIL);

        print(engine.execute("1 2+"));
        //print(engine.execute("'12' 12 ="));
        //ExecutorService service = Executors.newSingleThreadExecutor();
        /*Future<SharedArray> future = service.submit();

        try {
            SharedArray result = future.get(3, TimeUnit.SECONDS);
            print(result);
        } catch (TimeoutException e) {
            System.out.println("Timed out!");
            future.cancel(true);
        } finally {
            service.shutdown();
        }*/
    }

}
