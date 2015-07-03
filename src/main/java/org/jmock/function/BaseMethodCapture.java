package org.jmock.function;

import org.hamcrest.Matcher;
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

    protected W withMatchingParameters(Object... values) {
        return withMatching(new AllParametersMatcher(values));
    }

    protected W withMatchingParameters(Matcher<?>... matchers) {
        return withMatching(new AllParametersMatcher(Arrays.asList(matchers)));
    }

    protected W withMatchingParameters(Predicate...  predicates) {
        Stream<PredicateMatcher<Object>> predicateMatcherStream = Arrays.stream(predicates).map(PredicateMatcher::new);
        return withMatchingParameters(predicateMatcherStream.toArray(PredicateMatcher[]::new));
    }

    protected abstract W createWill(InvocationExpectationBuilder builder);

}
