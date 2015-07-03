package org.jmock.function;

import org.hamcrest.Matcher;
import org.jmock.internal.Cardinality;
import org.jmock.internal.InvocationExpectationBuilder;

import java.util.function.Predicate;

public class Proc1MethodCapture<P1, X extends Throwable> extends BaseMethodCapture<Proc1MethodCapture.Proc1Will> {

    public Proc1MethodCapture(Proc1<P1, X> proc, Cardinality cardinality, InvocationExpectationBuilder currentBuilder) {
        super(currentBuilder, cardinality);
        try {
            proc.apply(null); // captured by currentBuilder
        } catch (Throwable ignored) {
        }
    }

    public Proc1Will with(P1 p1) {
        return withMatchingParameters(p1);
    }

    public Proc1Will withMatching(Matcher<P1> m1) {
        return withMatchingParameters(m1);
    }

    public Proc1Will withMatching(Predicate<P1> p1) {
        return withMatchingParameters(p1);
    }

    protected Proc1Will createWill(InvocationExpectationBuilder builder) {
        return new Proc1Will(builder);
    }

    public class Proc1Will extends BaseWill {

        protected Proc1Will(InvocationExpectationBuilder currentBuilder) {
            super(currentBuilder);
        }

        public void will(FallibleCallable<X> callable) {
            will(new CallableAction<>(callable));
        }

        public void will(Proc1<P1, X> f) {
            will(new Proc1Action<>(f));
        }
    }

}
