package org.jmock.function;

import org.hamcrest.Matcher;
import org.jmock.internal.Cardinality;
import org.jmock.internal.InvocationExpectationBuilder;
import org.jmock.internal.ParametersMatcher;
import org.jmock.internal.matcher.AllParametersMatcher;

import java.util.Arrays;

public abstract class BaseMethodCapture<W extends BaseWill> {

    private final InvocationExpectationBuilder currentBuilder;

    public BaseMethodCapture(InvocationExpectationBuilder currentBuilder, Cardinality cardinality) {
        this.currentBuilder = currentBuilder;
        currentBuilder.setCardinality(cardinality);
    }


    protected void addParameterMatcher(ParametersMatcher parametersMatcher) {
        currentBuilder.addParameterMatcher(parametersMatcher);
    }

    protected ParametersMatcher allParametersMatcher(Matcher<?>... matchers) {
        return new AllParametersMatcher(Arrays.asList(matchers));
    }

    public W withMatching(ParametersMatcher parametersMatcher) {
        addParameterMatcher(parametersMatcher);
        return createWill(currentBuilder);
    }

    protected abstract W createWill(InvocationExpectationBuilder builder);

}
