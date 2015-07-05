package org.jmock.function;

import org.hamcrest.Matcher;
import org.jmock.function.internal.BaseMethodCapture;
import org.jmock.function.internal.BaseWill;
import org.jmock.internal.Cardinality;
import org.jmock.internal.InvocationExpectationBuilder;

import java.util.function.Predicate;

public class Func1MethodCapture<P1, R, X extends Throwable> extends BaseMethodCapture<Func1MethodCapture.Func1Will<P1, R, X>> {

    public Func1MethodCapture(Func1<P1, R, X> function, Cardinality cardinality, InvocationExpectationBuilder currentBuilder) {
        super(currentBuilder, cardinality);
        try {
            function.apply(null); // captured by currentBuilder
        } catch (Throwable ignored) {
        }
    }

    public Func1Will<P1, R, X> with(P1 p1) {
        return withParameterValues(p1);
    }

    public Func1Will<P1, R, X> withMatching(Matcher<P1> m1) {
        return withParameterMatchers(m1);
    }

    public Func1Will<P1, R, X> withMatching(Predicate<P1> p1) {
        return withParameterPredicates(p1);
    }

    protected Func1Will<P1, R, X> createWill(InvocationExpectationBuilder builder) {
        return new Func1Will<>(builder);
    }

    public static class Func1Will<P1, R, X extends Throwable> extends BaseWill {

        protected Func1Will(InvocationExpectationBuilder currentBuilder) {
            super(currentBuilder);
        }

        public void will(Func0<R, X> supplier) {
            will(new Func0Action<>(supplier));
        }

        public void will(Func1<P1, R, X> f) {
            will(new Func1Action<>(f));
        }
    }
}
