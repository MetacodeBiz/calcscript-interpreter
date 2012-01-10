
package biz.metacode.clients.calcscript.interpreter.builtins;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import biz.metacode.clients.calcscript.interpreter.Executable;
import biz.metacode.clients.calcscript.interpreter.execution.Context;

public enum StackOperators implements Executable {

    LEFT_SQUARE_BRACE {
        @Override
        public void execute(Context context) {
            context.markPosition();
        }
    },
    RIGHT_SQUARE_BRACE {
        @Override
        public void execute(Context context) {
            context.push(context.extractMarkedArray());
        }
    },
    DUPLICATE {
        @Override
        public void execute(Context context) {
            try {
                Serializable object = context.peek();

                ByteArrayOutputStream str = new ByteArrayOutputStream();
                (new ObjectOutputStream(str)).writeObject(object);

                ByteArrayInputStream str2 = new ByteArrayInputStream(str.toByteArray());
                context.push((Serializable) (new ObjectInputStream(str2)).readObject());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    },
    DROP {
        @Override
        public void execute(Context context) {
            context.pop();
        }
    },
    SWAP {
        @Override
        public void execute(Context context) {
            Serializable first = context.pop();
            Serializable second = context.pop();
            context.push(first);
            context.push(second);
        }
    }
}
