
package biz.metacode.calcscript.interpreter.builtins;

import biz.metacode.calcscript.interpreter.Invocable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Stores predefined set of operators.
 */
public final class Builtins {
    private Builtins() {
    }

    private static Map<String, Invocable> builtins = Collections
            .unmodifiableMap(new BuiltinsMap());

    private static final class BuiltinsMap extends HashMap<String, Invocable> {
        private static final long serialVersionUID = -8879873111905136912L;

        BuiltinsMap() {
            put("+", new CoercingDispatcher("+"));
            put("+_number_number", ArithmeticOperators.ADDITION);
            put("+_string_string", StringOperators.CONCATENATE);
            put("+_array_array", ArrayOperators.CONCATENATE);
            put("+_block_block", BlockOperators.CONCATENATE);

            put("-", new CoercingDispatcher("-"));
            put("-_number_number", ArithmeticOperators.SUBSTRACTION);
            put("-_array_array", ArrayOperators.SUBSTRACT);

            put("*", new OrderedDispatcher("*"));
            put("*_number_number", ArithmeticOperators.MULTIPLICATION);
            put("*_block_number", LoopOperators.TIMES);
            put("*_array_number", ArrayOperators.REPEAT);
            put("*_string_number", StringOperators.REPEAT);
            put("*_string_array", ArrayOperators.JOIN_BY_SEPARATOR);
            put("*_array_array", ArrayOperators.JOIN_ARRAYS);
            put("*_string_string", StringOperators.JOIN);
            put("*_block_array", ArrayOperators.FOLD);

            put("/", new OrderedDispatcher("/"));
            put("/_number_number", ArithmeticOperators.DIVISION);
            put("/_array_array", ArrayOperators.SPLIT_AROUND_MATCHES);
            // put("/_string_string",
            // StringOperators.SPLIT_AROUND_MATCHES);
            put("/_array_number", ArrayOperators.SPLIT_INTO_GROUPS);
            put("/_block_block", BlockOperators.UNFOLD);
            put("/_block_array", ArrayOperators.EACH);

            put("%", new OrderedDispatcher("%"));
            put("%_number_number", ArithmeticOperators.MODULO);
            put("%_block_array", ArrayOperators.MAP);
            put("%_array_number", ArrayOperators.EVERY_NTH_ELEMENT);
            put("%_string_string", StringOperators.SPLIT_AROUND_MATCHES_NONEMPTY);

            put("$", new SingleDispatcher("$"));
            put("$_number", StackOperators.GET_NTH);
            put("$_array", ArrayOperators.SORT);
            put("$_string", StringOperators.SORT_LETTERS);
            put("$_block", ArrayOperators.SORT_BY_MAPPING);

            put(",", new SingleDispatcher(","));
            put(",_array", ArrayOperators.GET_LENGTH);
            put(",_block", BlockOperators.FILTER);
            put(",_number", ArrayOperators.CREATE_ARRAY);

            put("~", new SingleDispatcher("~"));
            put("~_number", BitOperators.NOT);
            put("~_array", ArrayOperators.EXTRACT);
            put("~_block", BlockOperators.EXECUTE);
            put("~_string", StringOperators.EVAL);

            put("?", new OrderedDispatcher("?"));
            put("?_array_number", ArrayOperators.INDEX_OF);
            put("?_block_array", BlockOperators.FIND);
            put("?_number_number", ArithmeticOperators.POWER);

            put("|", new OrderedDispatcher("|"));
            put("|_array_array", ArrayOperators.UNION);
            put("|_number_number", BitOperators.OR);

            put("&", new OrderedDispatcher("&"));
            put("&_array_array", ArrayOperators.INTERSECTION);
            put("&_number_number", BitOperators.AND);

            put("^", new OrderedDispatcher("^"));
            put("^_array_array", ArrayOperators.SYMMETRIC_DIFFERENCE);
            put("^_number_number", BitOperators.XOR);

            put("<", new OrderedDispatcher("<"));
            put("<_array_number", ArrayOperators.INDEX_LESS_THAN);
            put("<_number_number", ComparisonOperators.LESS_THAN);
            put("<_string_string", ComparisonOperators.LESS_THAN);

            put(">", new OrderedDispatcher(">"));
            put(">_array_number", ArrayOperators.INDEX_GREATER_THAN);
            put(">_number_number", ComparisonOperators.GREATER_THAN);
            put(">_string_string", ComparisonOperators.GREATER_THAN);

            put("=", new OrderedDispatcher("="));
            put("=_number_number", ComparisonOperators.EQUALS);
            put("=_string_string", ComparisonOperators.EQUALS);
            put("=_array_number", ArrayOperators.GET_ELEMENT);

            put("(", new SingleDispatcher("("));
            put("(_array", ArrayOperators.UNCONS);
            put("(_number", ArithmeticOperators.DECREMENT);

            put(")", new SingleDispatcher(")"));
            put(")_array", ArrayOperators.UNCONS_RIGHT);
            put(")_number", ArithmeticOperators.INCREMENT);

            put("`", StringOperators.TO_STRING);
            put("!", BooleanOperators.NOT);

            put(".", StackOperators.DUPLICATE);
            put("@", StackOperators.ROT3);
            put("\\", StackOperators.SWAP);
            put(";", StackOperators.DROP);
            put("[", StackOperators.MARK_STACK_SIZE);
            put("]", StackOperators.SLICE_STACK);

            put("sum", MathOperators.SUM);
            put("rand", ArithmeticOperators.RANDOM);
            put("sin", ArithmeticOperators.SINUS);
            put("cos", ArithmeticOperators.COSINUS);
            put("do", LoopOperators.DO);
            put("abs", MathOperators.ABSOLUTE);

            put("zip", ArrayOperators.ZIP);
            put("or", BooleanOperators.OR);
            put("and", BooleanOperators.AND);
            put("xor", BooleanOperators.XOR);
            put("if", BooleanOperators.IF);
            put("while", LoopOperators.WHILE);
            put("until", LoopOperators.UNTIL);

            put("base", new OrderedDispatcher("#base"));
            put("#base_array_number", MathOperators.CONVERT_ARRAY_BASE);
            put("#base_number_number", MathOperators.CONVERT_NUMBER_BASE);
        }
    }

    /**
     * Return a read-only well-known configuration of operators.
     *
     * @return Operators and their standard names.
     */
    public static Map<String, Invocable> getBuiltins() {
        return builtins;
    }
}
