package org.jmock.function;

import org.jmock.internal.Cardinality;
import org.jmock.internal.InvocationExpectationBuilder;

public class Func0MethodCapture<R, X extends Throwable> extends BaseMethodCapture<Func0MethodCapture.Func0Will<R, X>> {

    public Func0MethodCapture(Func0<R, X> function, Cardinality cardinality, InvocationExpectationBuilder currentBuilder) {
        super(currentBuilder, cardinality);
        try {
            function.apply(); // captured by currentBuilder
        } catch (Throwable ignored) {
        }
    }

    public Func0Will<R, X> with() {
        return withParameterValues();
    }

    protected Func0Will<R, X> createWill(InvocationExpectationBuilder builder) {
        return new Func0Will<>(builder);
    }

    public static class Func0Will<R, X extends Throwable> extends BaseWill {

        protected Func0Will(InvocationExpectationBuilder currentBuilder) {
            super(currentBuilder);
        }

        public void will(Func0<R, X> function) {
            will(new Func0Action<>(function));
        }

    }
}
