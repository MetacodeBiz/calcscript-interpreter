
package biz.metacode.clients.calcscript.interpreter;

import java.io.Serializable;

/**
 * Named variable or constant.
 */
class Variable implements Expression, Serializable {

    private static final long serialVersionUID = 4937518097465718226L;

    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    /**
     * Executed when this variable is encountered during program execution. If
     * memory contains an {@link Invocable} object named like this variable it
     * is retrieved and invoked. Otherwise if this variable represents a numeric
     * or text constant it is interpreted like that and placed onto the stack.
     * If all of this fails this method exits.
     */
    public void evaluate(ExecutionContext context) {
        Invocable value = context.read(this.name);

        if (value != null) {
            value.invoke(context);
        } else {

            if (interpretAsDouble(context)) {
                return;
            }

            if (interpretAsString(context)) {
                return;
            }

        }
    }

    private boolean interpretAsDouble(ExecutionContext context) {
        try {
            context.pushDouble(Double.parseDouble(this.name));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean interpretAsString(ExecutionContext context) {
        if (isString(name)) {
            context.pushString(name.substring(1, this.name.length() - 1));
            return true;
        }
        return false;
    }

    private static boolean isString(String text) {
        return ('"' == text.charAt(0) && '"' == text.charAt(text.length() - 1))
                || ('\'' == text.charAt(0) && '\'' == text.charAt(text.length() - 1));
    }

    @Override
    public String toString() {
        return name;
    }

}
