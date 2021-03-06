package org.jmock.function.internal;

import org.hamcrest.Matcher;
import org.jmock.api.Action;
import org.jmock.api.Expectation;
import org.jmock.function.PredicateMatcher;
import org.jmock.internal.Cardinality;
import org.jmock.internal.InvocationExpectation;
import org.jmock.internal.InvocationExpectationBuilder;
import org.jmock.internal.ParametersMatcher;
import org.jmock.internal.matcher.AllParametersMatcher;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class BaseMethodCapture<W extends BaseWill> {

    private final InvocationExpectationBuilder expectationBuilderOrNull;
    private final Cardinality cardinality;
    protected ParametersMatcher parametersMatcher;
    private Action action;

    public BaseMethodCapture(InvocationExpectationBuilder expectationBuilderOrNull, Cardinality cardinality) {
        this.expectationBuilderOrNull = expectationBuilderOrNull;
        this.cardinality = cardinality;
    }

    protected void setParameterMatcher(ParametersMatcher parametersMatcher) {
        this.parametersMatcher = parametersMatcher;
    }

    public W withMatching(ParametersMatcher parametersMatcher) {
        setParameterMatcher(parametersMatcher);
        return createWill();
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

    protected abstract W createWill();

    protected void setAction(Action action) {
        this.action = action;
    }

    public Expectation toExpectation(Action defaultAction) {
        return toExpectation(expectationBuilderOrNull, defaultAction);
    }

    public boolean hasBuilder() {
        return expectationBuilderOrNull != null;
    }

    public Expectation toExpectation(InvocationExpectationBuilder anExpectationBuilder, Action defaultAction) {
        captureMethodInvoked();
        InvocationExpectation result = (InvocationExpectation) anExpectationBuilder.toExpectation(defaultAction);
        result.setCardinality(cardinality);
        if (action != null)
            result.setAction(action);
        if (parametersMatcher != null)
            result.setParametersMatcher(parametersMatcher);
        return result;
    }

    private void captureMethodInvoked() {
        try {
            invokeCapturedWithDummyParameters();
        } catch (Exception bad) {
            throw new RuntimeException(bad);
        }
    }

    protected abstract void invokeCapturedWithDummyParameters() throws Exception;
}
