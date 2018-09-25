
package biz.metacode.calcscript.interpreter.execution;

import biz.metacode.calcscript.interpreter.Invocable;
import biz.metacode.calcscript.interpreter.SharedArray;
import biz.metacode.calcscript.interpreter.Value;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * Script execution engine. Maintains a persistent memory for successive
 * executions. The engine is <em>not</em> thread safe.
 */
@NotThreadSafe
public class Engine {

    private Context context = new Context();

    /**
     * Constructs new script engine with empty memory.
     */
    public Engine() {
        context.setMemory(new Memory());
    }

    /**
     * Register an {@link Invocable} with given name.
     *
     * @param name Name of invocable.
     * @param executable {@link Invocable} to be registered.
     */
    public void register(@Nonnull final String name, @Nonnull final Invocable executable) {
        this.context.write(name, executable);
    }

    /**
     * Register many {@link Invocable} objects at once.
     *
     * @param executables Map of {@link Invocable}s.
     */
    public void register(@Nonnull final Map<String, Invocable> executables) {
        for (Map.Entry<String, Invocable> executable : executables.entrySet()) {
            register(executable.getKey(), executable.getValue());
        }
    }

    /**
     * Returns an {@link Invocable} registered with given name. May be
     * {@code null} if no {@link Invocable} is registered.
     *
     * @param name Variable name.
     * @return {@link Invocable} object or {@code null}.
     */
    @Nullable
    public Invocable read(@Nonnull final String name) {
        return this.context.read(name);
    }

    /**
     * Unregisters given variable.
     *
     * @param name Variable name.
     */
    public void remove(@Nonnull final String name) {
        this.context.remove(name);
    }

    /**
     * Executes a script and returns the result. It is important to use
     * {@link SharedArray#release()} after interacting with this method's
     * result.
     *
     * @param program Script to execute.
     * @return Array of values from the stack.
     * @throws InterruptedException If script execution was interrupted.
     */
    @Nonnull
    public SharedArray execute(@Nonnull final Invocable program)
            throws InterruptedException {
        context.clearStack();
        program.invoke(context);
        return context.getData();
    }

    /**
     * Returns a set of registered variable names.
     *
     * @return Set of registered variable names.
     */
    @Nonnull
    public Set<String> getVariableNames() {
        return context.getRegisteredVariableNames();
    }

    /**
     * Persists memory state to given stream.
     *
     * @param stream Stream to persist to.
     * @throws IOException When an IO operation fails.
     */
    public void saveState(@Nonnull final OutputStream stream) throws IOException {
        ObjectOutputStream objectOut = new ObjectOutputStream(stream);
        Map<String, Serializable> persistent = new HashMap<String, Serializable>();
        Iterator<Map.Entry<String, Invocable>> iterator = context
                .getRegisteredVariables();
        while (iterator.hasNext()) {
            Map.Entry<String, Invocable> object = iterator.next();
            if (object.getValue() instanceof Serializable) {
                persistent.put(object.getKey(), object.getValue());
            }
        }
        objectOut.writeObject(persistent);
    }

    /**
     * Restores the state of memory from given stream.
     *
     * @param stream Stream to read from.
     * @throws RestoreException Thrown when restoring fails.
     */
    public void restoreState(@Nonnull final InputStream stream) throws RestoreException {
        try {
            context.setMemory(new Memory());
            ObjectInputStream objectOut = new ObjectInputStream(stream);
            @SuppressWarnings("unchecked")
            Map<String, Serializable> objects = (Map<String, Serializable>) objectOut
                    .readObject();
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
        } catch (IllegalArgumentException e) {
            throw new RestoreException(e);
        }
    }

    /**
     * Clears the engine memory.
     */
    public void clearMemory() {
        context.setMemory(new Memory());
    }

    private EngineTestHelper testHelper;

    EngineTestHelper getTestHelper() {
        if (testHelper == null) {
            testHelper = new EngineTestHelper(context);
        }
        return testHelper;
    }
}
