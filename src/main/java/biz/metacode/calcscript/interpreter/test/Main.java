
package biz.metacode.calcscript.interpreter.test;

import biz.metacode.calcscript.interpreter.SharedArray;
import biz.metacode.calcscript.interpreter.Value;
import biz.metacode.calcscript.interpreter.builtins.Builtins;
import biz.metacode.calcscript.interpreter.execution.Engine;
import biz.metacode.calcscript.interpreter.execution.ScriptExecutionException;
import biz.metacode.calcscript.interpreter.source.SyntaxException;

import java.io.IOException;

public class Main {

    private static void print(SharedArray array) {
        if (array == null) {
            System.out.println("ERR");
            return;
        }
        System.out.println("Array: " + Integer.toHexString(System.identityHashCode(array)));
        try {
            for (Value out : array) {
                System.out.println("==> " + out);
            }
        } finally {
            array.release();
        }
    }

    private static final Engine engine = new Engine();

    private static SharedArray execute(String code) {
        try {
            return engine.execute(code);
        } catch (ScriptExecutionException e) {
        } catch (InterruptedException e) {
        } catch (SyntaxException e) {
        }
        return null;
    }

    /**
     * @param args
     * @throws ScriptExecutionException
     * @throws IOException
     */
    public static void main(String[] args) throws Exception {

        engine.register(Builtins.getBuiltins());

        //print(execute("1"));
        //print(execute("1 "));
        //print(execute("1 2"));
        //print(execute("1 2 "));
        //print(execute("1 2 3"));
        //print(execute("1 2 3]"));
        //print(execute("1 2 3]{"));
        //print(execute("1 2 3]{*"));
        //print(execute("1 2 3]{*}"));
        print(execute("1 2 3]{+}*"));
        // print(engine.execute("'12' 12 ="));
        // ExecutorService service = Executors.newSingleThreadExecutor();
        /*
         * Future<SharedArray> future = service.submit(); try { SharedArray
         * result = future.get(3, TimeUnit.SECONDS); print(result); } catch
         * (TimeoutException e) { System.out.println("Timed out!");
         * future.cancel(true); } finally { service.shutdown(); }
         */
    }

}
