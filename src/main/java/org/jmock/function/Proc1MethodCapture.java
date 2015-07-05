package org.jmock.function;

import org.hamcrest.Matcher;
import org.jmock.function.internal.BaseMethodCapture;
import org.jmock.function.internal.BaseWill;
import org.jmock.internal.Cardinality;
import org.jmock.internal.InvocationExpectationBuilder;

import java.util.function.Predicate;

public class Proc1MethodCapture<P1, X extends Throwable> extends BaseMethodCapture<Proc1MethodCapture.Proc1Will<P1, X>> {

    public Proc1MethodCapture(Proc1<P1, X> proc, Cardinality cardinality, InvocationExpectationBuilder currentBuilder) {
        super(currentBuilder, cardinality);
        try {
            proc.call(null); // captured by currentBuilder
        } catch (Throwable ignored) {
        }
    }

    public Proc1Will<P1, X> with(P1 p1) {
        return withParameterValues(p1);
    }

    public Proc1Will<P1, X> withMatching(Matcher<P1> m1) {
        return withParameterMatchers(m1);
    }

    public Proc1Will<P1, X> withMatching(Predicate<P1> p1) {
        return withParameterPredicates(p1);
    }

    protected Proc1Will<P1, X> createWill(InvocationExpectationBuilder builder) {
        return new Proc1Will<>(builder);
    }

    public static class Proc1Will<P1, X extends Throwable> extends BaseWill {

        protected Proc1Will(InvocationExpectationBuilder currentBuilder) {
            super(currentBuilder);
        }

        public void will(Proc0<X> callable) {
            will(new Proc0Action<>(callable));
        }

        public void will(Proc1<P1, X> f) {
            will(new Proc1Action<>(f));
        }
    }

}
