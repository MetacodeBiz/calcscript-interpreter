package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.ExecutionContext;
import biz.metacode.clients.calcscript.interpreter.Invocable;
import biz.metacode.clients.calcscript.interpreter.Value;

public enum StringOperators implements Invocable {
  CONCATENATE {

    public void invoke(ExecutionContext context) throws InterruptedException {
        Value first = context.pop();
        Value second = context.pop();
        try {
            context.pushString(second.toString() + first.toString());
        } finally {
            second.release();
            first.release();
        }
    }

  }
}
