
package biz.metacode.calcscript.interpreter.source;

import biz.metacode.calcscript.interpreter.ExecutionContext;
import biz.metacode.calcscript.interpreter.Invocable;
import biz.metacode.calcscript.interpreter.ScriptExecutionException;
import biz.metacode.calcscript.interpreter.SelfDescribing;

import java.io.Serializable;

/**
 * Named variable or constant.
 */
class Variable implements Expression, Serializable {

    private static final long serialVersionUID = 4937518097465718226L;

    private final String name;

    Variable(final String name) {
        this.name = name;
    }

    /**
     * Executed when this variable is encountered during program execution. If
     * memory contains an {@link Invocable} object named like this variable it
     * is retrieved and invoked. Otherwise if this variable represents a numeric
     * or text constant it is interpreted like that and placed onto the stack.
     * If all of this fails this method exits.
     *
     * @throws InterruptedException
     */
    public void evaluate(final ExecutionContext context) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }

        Invocable value = context.read(this.name);

        if (value != null) {
            try {
                value.invoke(context);
            } catch (ScriptExecutionException e) {
                String operatorName = e.getOperatorName();
                if (operatorName == null) {
                    operatorName = this.name;
                }
                String example = e.getExample();
                if (example == null) {
                    if (value instanceof SelfDescribing) {
                        example = ((SelfDescribing) value).getExampleUsage();
                    }
                }
                if (example == null) {
                    example = "3 1<name>";
                }
                example = example.replace("<name>", this.name);
                throw new ScriptExecutionException(this.name, example, e);
            }
        } else {

            if (interpretAsDouble(context)) {
                return;
            }

            if (interpretAsString(context)) {
                return;
            }

        }
    }

    private boolean interpretAsDouble(final ExecutionContext context) {
        try {
            context.pushDouble(Double.parseDouble(this.name));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean interpretAsString(final ExecutionContext context) {
        if (isString(name)) {
            context.pushString(name.substring(1, this.name.length() - 1));
            return true;
        }
        return false;
    }

    private static boolean isString(final String text) {
        return (text.length() >= 2)
                && (('"' == text.charAt(0) && '"' == text.charAt(text.length() - 1)) || ('\'' == text
                        .charAt(0) && '\'' == text.charAt(text.length() - 1)));
    }

    @Override
    public String toString() {
        return name;
    }

}
