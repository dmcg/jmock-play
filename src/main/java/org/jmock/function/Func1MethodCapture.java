package org.jmock.function;

import org.hamcrest.Matcher;
import org.jmock.function.internal.BaseMethodCapture;
import org.jmock.function.internal.BaseWill;
import org.jmock.function.internal.ParameterTypeFinder;
import org.jmock.internal.Cardinality;
import org.jmock.internal.InvocationExpectationBuilder;

import java.util.function.Predicate;

public class Func1MethodCapture<P1, R, X extends Exception> extends BaseMethodCapture<Func1MethodCapture.Func1Will<P1, R, X>> {

    private static final int ARITY = 1;

    private final Func1<P1, R, X> function;

    public Func1MethodCapture(Func1<P1, R, X> function, Cardinality cardinality, InvocationExpectationBuilder expectationBuilder) {
        super(expectationBuilder, cardinality);
        this.function = function;
    }

    public Func1Will<P1, R, X> with(P1 p1) {
        return withParameterValues(p1);
    }

    public Func1Will<P1, R, X> withMatching(Matcher<P1> p1) {
        return withParameterMatchers(p1);
    }

    public Func1Will<P1, R, X> withMatching(Predicate<P1> p1) {
        return withParameterPredicates(p1);
    }

    protected Func1Will<P1, R, X> createWill() {
        return new Func1Will<>(this);
    }

    @Override
    protected void invokeCapturedWithDummyParameters() throws X {
        Class[] parameterTypes = ParameterTypeFinder.findParameterTypes(function::applyArgs, ARITY);
        function.applyArgs(ParameterTypeFinder.argsWithClasses(parameterTypes));
    }

    public static class Func1Will<P1, R, X extends Exception> extends BaseWill {

        protected Func1Will(BaseMethodCapture<?> methodCapture) {
            super(methodCapture);
        }

        public void will(Func0<R, X> supplier) {
            will(supplier.asAction());
        }

        public void will(Func1<P1, R, X> f) {
            will(f.asAction());
        }
    }



}
