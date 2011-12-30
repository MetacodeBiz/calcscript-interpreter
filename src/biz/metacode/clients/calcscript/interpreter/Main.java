
package biz.metacode.clients.calcscript.interpreter;

import java.util.List;

public class Main {

    /**
     * @param args
     */
    public static void main(String[] args) {
        Engine engine = new Engine();
        List<Object> output = engine.execute("1 2 3 4");
        for (Object out : output) {
            System.out.println("==> " + out);
        }
    }

}
