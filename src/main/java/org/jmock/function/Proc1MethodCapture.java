package org.jmock.function;

import org.hamcrest.Matcher;
import org.jmock.function.internal.BaseMethodCapture;
import org.jmock.function.internal.BaseWill;
import org.jmock.function.internal.ParameterTypeFinder;
import org.jmock.internal.Cardinality;
import org.jmock.internal.InvocationExpectationBuilder;

import java.util.function.Predicate;

public class Proc1MethodCapture<P1, X extends Exception> extends BaseMethodCapture<Proc1MethodCapture.Proc1Will<P1, X>> {

    private static final int ARITY = 1;

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
    protected void invokeCapturedWithDummyParameters() throws X {
        Class[] parameterTypes = ParameterTypeFinder.findParameterTypes(proc::callWithArgs, ARITY);
        proc.callWithArgs(ParameterTypeFinder.argsWithClasses(parameterTypes));
    }

    protected Proc1Will<P1, X> createWill() {
        return new Proc1Will<>(this);
    }

    public static class Proc1Will<P1, X extends Exception> extends BaseWill {

        protected Proc1Will(BaseMethodCapture<?> methodCapture) {
            super(methodCapture);
        }

        public void will(Proc0<X> callable) {
            will(callable.asAction());
        }

        public void will(Proc1<P1, X> f) {
            will(f.asAction());
        }
    }

}
