
package biz.metacode.clients.calcscript.interpreter.builtins;

import biz.metacode.clients.calcscript.interpreter.Block;
import biz.metacode.clients.calcscript.interpreter.ExecutionContext;
import biz.metacode.clients.calcscript.interpreter.Invocable;

public enum BlockOperators implements Invocable {
    CONCATENATE {

        public void invoke(ExecutionContext context) throws InterruptedException {
            final Block first = (Block) context.pop();
            final Block second = (Block) context.pop();
            try {
                context.push(second.concatenate(first));
            } finally {
                second.release();
                first.release();
            }
        }

    }
}
