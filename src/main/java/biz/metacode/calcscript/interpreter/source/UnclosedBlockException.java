package biz.metacode.calcscript.interpreter.source;

public class UnclosedBlockException extends SyntaxException {

    private static final long serialVersionUID = 6293253614117453910L;

    public UnclosedBlockException() {
        super("Missing } sign.");
    }

}
