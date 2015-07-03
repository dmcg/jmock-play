package org.jmock.function;

import org.hamcrest.Matcher;
import org.jmock.internal.Cardinality;
import org.jmock.internal.InvocationExpectationBuilder;

import java.util.function.Predicate;

public class Function1MethodCapture<P1, R, X extends Throwable> extends BaseMethodCapture<Function1MethodCapture.Function1Will> {

    public Function1MethodCapture(Function1<P1, R, X> function, Cardinality cardinality, InvocationExpectationBuilder currentBuilder) {
        super(currentBuilder, cardinality);
        try {
            function.apply(null); // captured by currentBuilder
        } catch (Throwable ignored) {
        }
    }

    public Function1Will with(P1 p1) {
        return withMatchingParameters(p1);
    }

    public Function1Will withMatching(Matcher<P1> m1) {
        return withMatchingParameters(m1);
    }

    public Function1Will withMatching(Predicate<P1> p1) {
        return withMatchingParameters(p1);
    }

    protected Function1Will createWill(InvocationExpectationBuilder builder) {
        return new Function1Will(builder);
    }

    public class Function1Will extends BaseWill {

        protected Function1Will(InvocationExpectationBuilder currentBuilder) {
            super(currentBuilder);
        }

        public void will(FallibleSupplier<R, X> supplier) {
            will(new SupplierAction<>(supplier));
        }

        public void will(Function1<P1, R, X> f) {
            will(new Function1Action<>(f));
        }
    }
}
