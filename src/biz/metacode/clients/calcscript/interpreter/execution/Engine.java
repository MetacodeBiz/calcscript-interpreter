
package biz.metacode.clients.calcscript.interpreter.execution;

import biz.metacode.clients.calcscript.interpreter.Executable;
import biz.metacode.clients.calcscript.interpreter.Program;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Set;

public class Engine {

    private Memory memory = new Memory();

    public void register(String name, Executable executable) {
        this.memory.write(name, executable);
    }

    public List<Serializable> execute(CharSequence source) throws ExecutionException {
        Context context = new Context(memory);
        Program program = new Program(source);
        try {
            program.execute(context);
        } catch (EmptyStackException e) {
            throw new ExecutionException("Stack is empty", e);
        } catch (ClassCastException e) {
            throw new ExecutionException("Wrong type", e);
        }
        return context.getData();
    }

    public Set<String> getVariableNames() {
        return memory.keys();
    }

    public void saveState(OutputStream stream) throws IOException {
        ObjectOutputStream objectOut = new ObjectOutputStream(stream);
        objectOut.writeObject(memory);
    }

    public void restoreState(InputStream stream) throws IOException {
        ObjectInputStream objectOut = new ObjectInputStream(stream);
        try {
            memory = (Memory) objectOut.readObject();
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    public void clearMemory() {
        memory = new Memory();
    }
}
