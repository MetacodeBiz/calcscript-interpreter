
package biz.metacode.calcscript.interpreter.shell;

import biz.metacode.calcscript.interpreter.ScriptExecutionException;
import biz.metacode.calcscript.interpreter.SharedArray;
import biz.metacode.calcscript.interpreter.Value;
import biz.metacode.calcscript.interpreter.builtins.Builtins;
import biz.metacode.calcscript.interpreter.execution.Engine;
import biz.metacode.calcscript.interpreter.source.SyntaxException;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Runs Calcscript interactive interpreter.
 */
public class Main {

    private Main() {

    }

    private static void print(final SharedArray array) {
        if (array == null) {
            System.out.println("ERR");
            return;
        }
        try {
            for (Value out : array) {
                System.out.println("=> " + out);
            }
        } finally {
            array.release();
        }
    }

    private static final Engine ENGINE = new Engine();

    private static SharedArray execute(final String code) {
        try {
            return ENGINE.execute(code);
        } catch (ScriptExecutionException e) {
            System.err.println(e.getMessage() + " Example: " + e.getExample());
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        } catch (SyntaxException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    /**
     * Runs Calcscript interactive console.
     *
     * @param args Console arguments.
     * @throws Exception Propagated exception.
     */
    public static void main(final String... args) throws Exception {

        ENGINE.register(Builtins.getBuiltins());

        final InputStreamReader converter = new InputStreamReader(System.in, "UTF-8");
        final BufferedReader in = new BufferedReader(converter);

        System.out.println("   Calcscript interactive interpreter.");
        System.out.println("   Type \"quit\" to exit.");
        System.out.println();

        do {
            System.out.print("-> ");
            String line = in.readLine();

            if (line == null || "".equals(line) || "quit".equals(line)) {
                break;
            }

            print(execute(line));

        } while (true);

    }
}
