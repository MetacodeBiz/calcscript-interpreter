
package biz.metacode.clients.calcscript.interpreter.execution;

import biz.metacode.clients.calcscript.interpreter.Invocable;
import biz.metacode.clients.calcscript.interpreter.Program;
import biz.metacode.clients.calcscript.interpreter.SharedArray;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Engine {

    private Memory memory = new Memory();

    private Context context = new Context();

    public void register(String name, Invocable executable) {
        this.memory.write(name, executable);
    }

    public SharedArray execute(CharSequence source) throws ExecutionException {
        Program program = new Program(source);
        try {
            context.setMemory(memory);
            context.clearStack();
            program.invoke(context);
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
        Map<String, Serializable> persistent = new HashMap<String, Serializable>();
        for (Map.Entry<String, Invocable> object : memory) {
            if (object.getValue() instanceof Serializable) {
                persistent.put(object.getKey(), (Serializable) object.getValue());
            }
        }
        objectOut.writeObject(persistent);
    }

    public void restoreState(InputStream stream) throws RestoreException {
        try {
            Memory memory = new Memory();
            ObjectInputStream objectOut = new ObjectInputStream(stream);
            @SuppressWarnings("unchecked")
            Map<String, Serializable> objects = (Map<String, Serializable>) objectOut.readObject();
            for (Map.Entry<String, Serializable> entry : objects.entrySet()) {
                if (entry.getValue() instanceof Invocable) {
                    Invocable object = (Invocable) entry.getValue();
                    if (object instanceof PooledObject) {
                        ((PooledObject) object).attachToPool(context);
                    }
                    memory.write(entry.getKey(), object);
                }
            }
            this.memory = memory;
        } catch (ClassCastException e) {
            throw new RestoreException(e);
        } catch (ClassNotFoundException e) {
            throw new RestoreException(e);
        } catch (IOException e) {
            throw new RestoreException(e);
        }
    }

    public void clearMemory() {
        memory = new Memory();
    }
}
