
package biz.metacode.clients.calcscript.interpreter;

public class Variable implements Visitable {

    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public void visit(ExecutionContext context) {
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
