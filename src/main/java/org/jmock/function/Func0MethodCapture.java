package org.jmock.function;

import org.jmock.function.internal.BaseMethodCapture;
import org.jmock.function.internal.BaseWill;
import org.jmock.internal.Cardinality;
import org.jmock.internal.InvocationExpectationBuilder;

public class Func0MethodCapture<R, X extends Exception> extends BaseMethodCapture<Func0MethodCapture.Func0Will<R, X>> {

    private final Func0<R, X> function;

    public Func0MethodCapture(Func0<R, X> function, Cardinality cardinality, InvocationExpectationBuilder currentBuilder) {
        super(currentBuilder, cardinality);
        this.function = function;
    }

    public Func0Will<R, X> with() {
        return withParameterValues();
    }

    @Override
    protected void invokeCapturedWithDummyParameters() throws X {
        function.apply();
    }

    protected Func0Will<R, X> createWill() {
        return new Func0Will<>(this);
    }

    public static class Func0Will<R, X extends Exception> extends BaseWill {

        protected Func0Will(BaseMethodCapture<?> methodCapture) {
            super(methodCapture);
        }

        public void will(Func0<R, X> function) {
            will(function.asAction());
        }

    }
}
