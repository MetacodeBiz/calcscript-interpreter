
package biz.metacode.clients.calcscript.interpreter;

import biz.metacode.clients.calcscript.interpreter.execution.Context;

public class Variable implements Visitable {

    private static final long serialVersionUID = 2506602173741080789L;

    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public void visit(Context context) {
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

    private boolean interpretAsDouble(Context context) {
        try {
            context.push(Double.parseDouble(this.name));
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean interpretAsString(Context context) {
        if (isString(name)) {
            context.push(name.substring(1, this.name.length() - 1));
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
