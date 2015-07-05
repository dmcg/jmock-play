package org.jmock.function.internal;

import org.jmock.api.Action;
import org.jmock.internal.InvocationExpectationBuilder;

public class BaseWill {

    private final InvocationExpectationBuilder currentBuilder;

    protected BaseWill(InvocationExpectationBuilder currentBuilder) {
        this.currentBuilder = currentBuilder;
    }

    public void will(Action action) {
        currentBuilder.setAction(action);
    }
}
