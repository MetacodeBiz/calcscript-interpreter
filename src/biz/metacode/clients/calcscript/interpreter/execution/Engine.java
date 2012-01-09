
package biz.metacode.clients.calcscript.interpreter.execution;

import biz.metacode.clients.calcscript.interpreter.Executable;
import biz.metacode.clients.calcscript.interpreter.Program;

import java.util.EmptyStackException;
import java.util.List;
import java.util.Set;

public class Engine {

    private final Memory memory = new Memory();

    public void register(String name, Executable executable) {
        this.memory.write(name, executable);
    }

    public List<Object> execute(CharSequence source) throws ExecutionException {
        Context context = new Context(memory);
        Program program = new Program(source);
        try {
            program.execute(context);
        } catch (EmptyStackException e) {
            throw new ExecutionException(e);
        } catch (ClassCastException e) {
            throw new ExecutionException(e);
        }
        return context.getData();
    }

    public Set<String> getVariableNames() {
        return memory.keys();
    }
}
