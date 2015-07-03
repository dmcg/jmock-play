package org.jmock.function;

import org.hamcrest.Matcher;
import org.jmock.internal.Cardinality;
import org.jmock.internal.InvocationExpectationBuilder;
import org.jmock.internal.ParametersMatcher;
import org.jmock.internal.matcher.AllParametersMatcher;

import java.util.function.Predicate;

public class Proc1MethodCapture<P1, X extends Throwable> extends BaseMethodCapture {
    public Proc1MethodCapture(Proc1<P1, X> function, Cardinality cardinality, InvocationExpectationBuilder currentBuilder) {
        super(currentBuilder, cardinality);
        try {
            function.apply(null); // captured by currentBuilder
        } catch (Throwable ignored) {
        }
    }

    public Will with(P1 p1) {
        return withMatching(new AllParametersMatcher(new Object[]{p1}));
    }

    public Will withMatching(Matcher<P1> p1) {
        return withMatching(allParametersMatcher(p1));
    }

    public Will withMatching(Predicate<P1> p1) {
        return withMatching(new PredicateMatcher<>(p1));
    }

    public Will withMatching(ParametersMatcher parametersMatcher) {
        addParameterMatcher(parametersMatcher);
        return new Will();
    }

    public class Will extends BaseWill {
        public void will(FallibleCallable<X> callable) {
            will(new CallableAction<>(callable));
        }

        public void will(Proc1<P1, X> f) {
            will(new Proc1Action<>(f));
        }
    }
}
