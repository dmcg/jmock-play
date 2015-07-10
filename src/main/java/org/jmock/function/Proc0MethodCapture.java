package org.jmock.function;

import org.jmock.function.internal.BaseMethodCapture;
import org.jmock.function.internal.BaseWill;
import org.jmock.internal.Cardinality;
import org.jmock.internal.InvocationExpectationBuilder;

public class Proc0MethodCapture<X extends Exception> extends BaseMethodCapture<Proc0MethodCapture.Proc0Will<X>> {

    private final Proc0<X> function;

    public Proc0MethodCapture(Proc0<X> function, Cardinality cardinality, InvocationExpectationBuilder currentBuilder) {
        super(currentBuilder, cardinality);
        this.function = function;
    }

    public Proc0Will<X> with() {
        return withParameterValues();
    }

    @Override
    protected void invokeCapturedWithDummyParameters() throws Exception {
        function.call();
    }

    protected Proc0Will<X> createWill() {
        return new Proc0Will<>(this);
    }

    public static class Proc0Will<X extends Exception> extends BaseWill {

        protected Proc0Will(BaseMethodCapture<?> methodCapture) {
            super(methodCapture);
        }

        public void will(Proc0<X> callable) {
            will(callable.asAction());
        }
    }
}
