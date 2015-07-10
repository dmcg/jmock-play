package org.jmock.function;

import org.hamcrest.Matcher;
import org.jmock.function.internal.BaseMethodCapture;
import org.jmock.function.internal.BaseWill;
import org.jmock.internal.Cardinality;
import org.jmock.internal.InvocationExpectationBuilder;

import java.util.function.Predicate;

public class Proc1MethodCapture<P1, X extends Exception> extends BaseMethodCapture<Proc1MethodCapture.Proc1Will<P1, X>> {

    private final Proc1<P1, X> proc;

    public Proc1MethodCapture(Proc1<P1, X> proc, Cardinality cardinality, InvocationExpectationBuilder currentBuilder) {
        super(currentBuilder, cardinality);
        this.proc = proc;
    }

    public Proc1Will<P1, X> with(P1 p1) {
        return withParameterValues(p1);
    }

    public Proc1Will<P1, X> withMatching(Matcher<P1> p1) {
        return withParameterMatchers(p1);
    }

    public Proc1Will<P1, X> withMatching(Predicate<P1> p1) {
        return withParameterPredicates(p1);
    }

    @Override
    protected void invokeCapturedWithDummyParameters() throws Exception {
        proc.call(null);
    }

    protected Proc1Will<P1, X> createWill(InvocationExpectationBuilder builder) {
        return new Proc1Will<>(builder);
    }

    public static class Proc1Will<P1, X extends Exception> extends BaseWill {

        protected Proc1Will(InvocationExpectationBuilder currentBuilder) {
            super(currentBuilder);
        }

        public void will(Proc0<X> callable) {
            will(callable.asAction());
        }

        public void will(Proc1<P1, X> f) {
            will(f.asAction());
        }
    }

}
