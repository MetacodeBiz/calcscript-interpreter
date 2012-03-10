/**
 * Contains basic types that all other packages use.
 * <p>
 * The interface {@link biz.metacode.calcscript.interpreter.Invocable} represents the most primitive
 * type of an object in the interpreter - an operation that can be executed when called by name.
 * This is an interface that all custom operators implement.
 * {@link biz.metacode.calcscript.interpreter.Invocable} objects can be stored in script's memory
 * using {@link biz.metacode.calcscript.interpreter.ExecutionContext#write(String, Invocable)}
 * <p>
 * Second basic type is a {@link biz.metacode.calcscript.interpreter.Value}. Value represents a data
 * structure that is exchanged by operators when executing the script. Values are stored in script's
 * stack. The language defines four basic types of values. They are defined in
 * {@link biz.metacode.calcscript.interpreter.Value.Type}.
 * {@link biz.metacode.calcscript.interpreter.SharedArray} is a special interface implemented by
 * values that act as a collection of other values.
 * <p>
 * {@link biz.metacode.calcscript.interpreter.ExecutionContext} represents current state of
 * script execution. Operators communicate with each other by using the context's stack.
 */

package biz.metacode.calcscript.interpreter;

