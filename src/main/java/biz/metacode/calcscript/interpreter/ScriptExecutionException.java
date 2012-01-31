
package biz.metacode.calcscript.interpreter;

public class ScriptExecutionException extends RuntimeException {

    private static final long serialVersionUID = -2441264131952720908L;

    private String operatorName;

    private String example;

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

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }
}
