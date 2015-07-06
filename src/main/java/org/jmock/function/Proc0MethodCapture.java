package org.jmock.function;

import org.jmock.function.internal.BaseMethodCapture;
import org.jmock.function.internal.BaseWill;
import org.jmock.internal.Cardinality;
import org.jmock.internal.InvocationExpectationBuilder;

public class Proc0MethodCapture<X extends Exception> extends BaseMethodCapture<Proc0MethodCapture.Proc0Will<X>> {

    public Proc0MethodCapture(Proc0<X> function, Cardinality cardinality, InvocationExpectationBuilder currentBuilder) {
        super(currentBuilder, cardinality);
        try {
            function.call(); // captured by currentBuilder
        } catch (Throwable ignored) {
        }
    }

    public Proc0Will<X> with() {
        return withParameterValues();
    }

    protected Proc0Will<X> createWill(InvocationExpectationBuilder builder) {
        return new Proc0Will<>(builder);
    }

    public static class Proc0Will<X extends Exception> extends BaseWill {

        protected Proc0Will(InvocationExpectationBuilder currentBuilder) {
            super(currentBuilder);
        }

        public void will(Proc0<X> callable) {
            will(new Proc0Action<>(callable));
        }
    }
}
