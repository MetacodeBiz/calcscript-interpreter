
package biz.metacode.calcscript.interpreter;

public class ScriptExecutionException extends RuntimeException {

    private static final long serialVersionUID = -2441264131952720908L;

    private String operatorName;

    public ScriptExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScriptExecutionException(String message) {
        super(message);
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
}
