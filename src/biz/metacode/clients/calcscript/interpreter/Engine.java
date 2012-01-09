
package biz.metacode.clients.calcscript.interpreter;

import java.util.EmptyStackException;
import java.util.List;
import java.util.Set;

public class Engine {

    private final Memory memory = new Memory();

    private Stack stack;

    public void register(String name, Executable executable) {
        this.memory.write(name, executable);
    }

    public List<Object> execute(CharSequence source) throws ExecutionException {
        this.stack = new Stack();
        Program program = new Program(source);
        try {
            program.execute(stack, memory);
        } catch (EmptyStackException e) {
            throw new ExecutionException(e);
        }
        return stack.getData();
    }

    public Set<String> getVariableNames() {
        return memory.keys();
    }
}
