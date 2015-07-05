package org.jmock.function.internal;

import org.hamcrest.Matcher;
import org.jmock.function.PredicateMatcher;
import org.jmock.internal.Cardinality;
import org.jmock.internal.InvocationExpectationBuilder;
import org.jmock.internal.ParametersMatcher;
import org.jmock.internal.matcher.AllParametersMatcher;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class BaseMethodCapture<W extends BaseWill> {

    private final InvocationExpectationBuilder currentBuilder;

    public BaseMethodCapture(InvocationExpectationBuilder currentBuilder, Cardinality cardinality) {
        this.currentBuilder = currentBuilder;
        currentBuilder.setCardinality(cardinality);
    }


    protected void addParameterMatcher(ParametersMatcher parametersMatcher) {
        currentBuilder.addParameterMatcher(parametersMatcher);
    }

    public W withMatching(ParametersMatcher parametersMatcher) {
        addParameterMatcher(parametersMatcher);
        return createWill(currentBuilder);
    }

    protected W withParameterValues(Object... values) {
        return withMatching(new AllParametersMatcher(values));
    }

    protected W withParameterMatchers(Matcher<?>... matchers) {
        return withMatching(new AllParametersMatcher(Arrays.asList(matchers)));
    }

    @SuppressWarnings({"Convert2MethodRef", "unchecked"})
    protected W withParameterPredicates(Predicate... predicates) {
        Stream<PredicateMatcher<Object>> predicateMatcherStream = Arrays.stream(predicates).map(p -> new PredicateMatcher<>(p));
        return withParameterMatchers(predicateMatcherStream.toArray(PredicateMatcher[]::new));
    }

    protected abstract W createWill(InvocationExpectationBuilder builder);
}
