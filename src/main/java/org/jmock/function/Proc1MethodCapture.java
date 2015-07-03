package org.jmock.function;

import org.hamcrest.Matcher;
import org.jmock.internal.Cardinality;
import org.jmock.internal.InvocationExpectationBuilder;
import org.jmock.internal.matcher.AllParametersMatcher;

import java.util.function.Predicate;

public class Proc1MethodCapture<P1, X extends Throwable> extends BaseMethodCapture<Proc1Will> {

    public Proc1MethodCapture(Proc1<P1, X> function, Cardinality cardinality, InvocationExpectationBuilder currentBuilder) {
        super(currentBuilder, cardinality);
        try {
            function.apply(null); // captured by currentBuilder
        } catch (Throwable ignored) {
        }
    }

    public Proc1Will with(P1 p1) {
        return withMatching(new AllParametersMatcher(new Object[]{p1}));
    }

    public Proc1Will withMatching(Matcher<P1> p1) {
        return withMatching(allParametersMatcher(p1));
    }

    public Proc1Will withMatching(Predicate<P1> p1) {
        return withMatching(new PredicateMatcher<>(p1));
    }

    protected Proc1Will createWill(InvocationExpectationBuilder builder) {
        return new Proc1Will(builder);
    }

}
