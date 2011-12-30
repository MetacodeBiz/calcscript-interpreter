
package biz.metacode.clients.calcscript.interpreter;

import java.util.List;

public class Engine {

    private final Stack stack = new Stack();

    private final Memory memory = new Memory();

    public List<Object> execute(CharSequence source) {
        Program program = new Program(source);
        program.execute(stack, memory);
        return stack.getData();
    }

    private static final class Program implements Executable {

        private final Parser parser;

        public Program(CharSequence source) {
            this.parser = new Parser(source);
        }

        @Override
        public void execute(Stack stack, Memory memory) {
            for (Visitable visitable : parser) {
                visitable.visit(stack, memory);
            }
        }

    }
}
