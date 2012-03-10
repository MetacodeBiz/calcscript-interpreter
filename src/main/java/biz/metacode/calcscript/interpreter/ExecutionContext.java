
package biz.metacode.calcscript.interpreter;

import biz.metacode.calcscript.interpreter.Value.Pair;

import java.util.Collection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Context of program execution. Represents state of memory (read/write) and
 * stack (push/pop).
 */
public interface ExecutionContext {

    /**
     * Assigns a name to a given object in memory or unsets a variable.
     *
     * @param name Variable name.
     * @param object Object that should be stored or {@code null} if the
     *            variable should be erased.
     */
    void write(@Nonnull String name, @Nullable Invocable object);

    /**
     * Returns an object for given name or {@code null} if this name does not
     * have any object assigned.
     *
     * @param name Variable name.
     * @return Object stored under that name or {@code null} if name does not
     *         represent an object.
     */
    @Nullable
    Invocable read(@Nonnull String name);

    /**
     * Pushes a value onto the stack.
     *
     * @param value Value that should be pushed.
     */
    void push(@Nonnull Value value);

    /**
     * Retrieves and removes a value from the top of the stack. This method
     * never returns {@code null} but throws {@link ValueMissingException} if
     * the stack is empty.
     * <p>
     * It is important to call {@link Value#release()} on the returned object
     * when it is no longer needed.
     *
     * @return Value from the top of the stack.
     */
    @Nonnull
    Value pop();

    /**
     * Returns the value on the n-th position counting from the top (0 is the
     * highest value). Returned value is not removed from the stack and should
     * not be released! Method may throw {@link ValueMissingException} if
     * position is invalid or the stack is empty.
     *
     * @param n Position of the value relative to the top of the stack.
     * @return Value the occupies given position.
     */
    @Nonnull
    Value peekAt(int n);

    /**
     * Returns the value on the top of the stack but does not remove it.
     * Returned value is not removed from the stack and should not be released!
     * Method may throw {@link ValueMissingException} if position is invalid or
     * the stack is empty.
     *
     * @return Value at the top of the stack.
     */
    Value peek();

    /**
     * Pushes a given number onto the stack.
     *
     * @param value Value that should be pushed.
     */
    void pushDouble(double value);

    /**
     * Pops a value from the top of the stack and returns it if it is a number
     * throws {@link InvalidTypeException} otherwise. Method may throw
     * {@link ValueMissingException} if the stack is empty.
     *
     * @return Value from the top of the stack.
     */
    double popDouble();

    /**
     * Pushes a given string onto the stack
     *
     * @param value Value that should be pushed
     */
    void pushString(@Nonnull String value);

    /**
     * Pops a value from the top of the stack and returns it if it is a string
     * throws {@link InvalidTypeException} otherwise. Method may throw
     * {@link ValueMissingException} if the stack is empty.
     *
     * @return Value from the top of the stack.
     */
    String popString();

    /**
     * Pushes a number 1 if value is {@code true} or number 0 if value is
     * {@code false}.
     *
     * @param value Value that should be pushed
     */
    void pushBoolean(boolean value);

    /**
     * Pops a value from the top of the stack and converts it to boolean. The
     * conversion depends on the type of {@link Value}. Method may throw
     * {@link ValueMissingException} if the stack is empty.
     *
     * @return Value from the top of the stack.
     */
    boolean popBoolean();

    /**
     * Pushes a collection as an array onto the stack. Collection will be
     * converted to {@link SharedArray} if necessary.
     *
     * @param array Collection to push.
     */
    void pushArray(@Nonnull Collection<? extends Value> array);

    /**
     * Creates and returns new empty array.
     *
     * @return New empty array.
     */
    @Nonnull
    SharedArray acquireArray();

    /**
     * Converts a collection to {@link Value} object. Useful when an array needs
     * to be stored inside {@link SharedArray}.
     *
     * @param array Collection to be converted.
     * @return Converted array.
     */
    @Nonnull
    Value convertToValue(@Nonnull Collection<? extends Value> array);

    /**
     * Converts string to {@link Value} object.
     *
     * @param string String to be converted.
     * @return String value.
     */
    @Nonnull
    Value convertToValue(@Nonnull String string);

    /**
     * Converts a number to {@link Value} object.
     *
     * @param value Number to be converted.
     * @return Converted value.
     */
    @Nonnull
    Value convertToValue(double value);

    /**
     * Coerces {@code first} and {@code second} to be of the same type.
     *
     * @param first First value.
     * @param second Second value.
     * @return Coerced pair of values.
     */
    @Nonnull
    Pair coerce(@Nonnull Value first, @Nonnull Value second);

    /**
     * Marks a current position of the stack to be later sliced using
     * {@link #extractMarkedArray()}.
     */
    void markPosition();

    /**
     * Slices the stack at the position marked by {@link #markPosition()} or at
     * the bottom of the stack if there are no marked positions.
     *
     * @return Array containing stack entries.
     */
    SharedArray extractMarkedArray();

    /**
     * Check if script execution should be interrupted. This method should be
     * called within loops or any kind of code that may never finish.
     *
     * @throws InterruptedException If script execution should be interrupted.
     */
    void interruptionPoint() throws InterruptedException;
}
