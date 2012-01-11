
package biz.metacode.clients.calcscript.interpreter;

import biz.metacode.clients.calcscript.interpreter.execution.Context;
import biz.metacode.clients.calcscript.interpreter.execution.Value;

public class Variable implements Visitable {

    private static final long serialVersionUID = 2506602173741080789L;

    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public void visit(Context context) {
        Value value = context.read(this.name);
        if (value != null) {
            if (value instanceof Executable) {
                ((Executable) value).execute(context);
            } else {
                context.push(value);
            }
        } else {
            try {
                context.push(Double.parseDouble(this.name));
                return;
            } catch (NumberFormatException e) {

            }

            if (('"' == this.name.charAt(0) && '"' == this.name.charAt(this.name.length() - 1))
                    || ('\'' == this.name.charAt(0) && '\'' == this.name
                            .charAt(this.name.length() - 1))) {
                context.push(name.substring(1, this.name.length() - 1));
                return;
            }

        }
    }

    @Override
    public String toString() {
        return name;
    }

}
