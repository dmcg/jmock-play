package org.jmock.function;

import org.hamcrest.Description;
import org.jmock.api.Action;
import org.jmock.api.Invocation;

public class Func1Action<P1, R, X extends Exception> implements Action {
    private final Func1<P1, R, X> function;

    public Func1Action(Func1<P1, R, X> function) {
        this.function = function;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Invocation invocation) throws Throwable {
        return function.apply((P1) invocation.getParameter(0));
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("invoke a function");

    }
}
