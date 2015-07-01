package org.jmock.function;

import org.hamcrest.Description;
import org.jmock.api.Action;
import org.jmock.api.Invocation;

public class Function1Action<P1, R, X extends Throwable> implements Action {
    private final Function1<P1, R, X> function;

    public Function1Action(Function1<P1, R, X> function) {
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
