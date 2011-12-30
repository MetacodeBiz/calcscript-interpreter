
package biz.metacode.clients.calcscript.interpreter;

public class Variable implements Visitable {

    private final String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public void visit(Stack stack, Memory memory) {
        Object value = memory.read(this.name);
        if (value != null) {
            if (value instanceof Executable) {
                ((Executable) value).execute(stack, memory);
            } else {
                stack.push(value);
            }
        } else {
            try {
                Integer v = Integer.valueOf(this.name);
                stack.push(v);
                return;
            } catch (NumberFormatException e) {

            }

            if (('"' == this.name.charAt(0) && '"' == this.name.charAt(this.name.length() - 1))
                    || ('\'' == this.name.charAt(0) && '\'' == this.name
                            .charAt(this.name.length() - 1))) {
                stack.push(name.substring(1, this.name.length() - 1));
                return;
            }

        }
    }

}
