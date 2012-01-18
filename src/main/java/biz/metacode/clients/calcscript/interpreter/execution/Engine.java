
package biz.metacode.clients.calcscript.interpreter.execution;

import biz.metacode.clients.calcscript.interpreter.Invocable;
import biz.metacode.clients.calcscript.interpreter.Program;
import biz.metacode.clients.calcscript.interpreter.SharedArray;
import biz.metacode.clients.calcscript.interpreter.Value;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

public class Engine {

    private Context context = new Context();

    public Engine() {
        context.setMemory(new Memory());
    }

    public void register(String name, Invocable executable) {
        this.context.write(name, executable);
    }

    public SharedArray execute(CharSequence source) throws ExecutionException, InterruptedException {
        Program program = new Program(source);
        try {
            context.clearStack();
            program.invoke(context);
        } catch (EmptyStackException e) {
            throw new ExecutionException("Stack is empty", e);
        } catch (ClassCastException e) {
            throw new ExecutionException("Wrong type", e);
        }
        return context.getData();
    }

    public Callable<SharedArray> executeLater(final CharSequence source) {
        return new Callable<SharedArray>() {
            public SharedArray call() throws Exception {
                return Engine.this.execute(source);
            }
        };
    }

    public Set<String> getVariableNames() {
        return context.getRegisteredVariableNames();
    }

    public void saveState(OutputStream stream) throws IOException {
        ObjectOutputStream objectOut = new ObjectOutputStream(stream);
        Map<String, Serializable> persistent = new HashMap<String, Serializable>();
        Iterator<Map.Entry<String, Invocable>> iterator = context.getRegisteredVariables();
        while (iterator.hasNext()) {
            Map.Entry<String, Invocable> object = iterator.next();
            if (object.getValue() instanceof Serializable) {
                persistent.put(object.getKey(), (Serializable) object.getValue());
            }
        }
        objectOut.writeObject(persistent);
    }

    public void restoreState(InputStream stream) throws RestoreException {
        try {
            context.setMemory(new Memory());
            ObjectInputStream objectOut = new ObjectInputStream(stream);
            @SuppressWarnings("unchecked")
            Map<String, Serializable> objects = (Map<String, Serializable>) objectOut.readObject();
            for (Map.Entry<String, Serializable> entry : objects.entrySet()) {
                if (entry.getValue() instanceof Invocable) {
                    Invocable object = (Invocable) entry.getValue();
                    if (object instanceof PooledObject) {
                        ((PooledObject) object).attachToPool(context);
                    }
                    context.write(entry.getKey(), object);
                    // writing value to memory will increase its reference
                    // counter
                    // above what it had when was serialized
                    if (object instanceof Value) {
                        ((Value) object).release();
                    }
                }
            }
        } catch (ClassCastException e) {
            throw new RestoreException(e);
        } catch (ClassNotFoundException e) {
            throw new RestoreException(e);
        } catch (IOException e) {
            throw new RestoreException(e);
        }
    }

    public void clearMemory() {
        context.setMemory(new Memory());
    }

    EngineTestHelper getTestHelper() {
        return new EngineTestHelper(context);
    }
}
